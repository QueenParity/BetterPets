package com.kingparity.betterpets.blockentity;

import com.kingparity.betterpets.init.ModBlockEntities;
import com.kingparity.betterpets.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FluidPipeBlockEntity extends BlockEntity implements IFluidTankWriter
{
    protected final int[] links = new int[6];
    
    public static final int SECTION_CAPACITY = 80;
    public static final int TRANSFER_RATE = 20;
    public static final int TRANSFER_DELAY = 10;
    public static final int DIRECTION_COOLDOWN = 60;
    public static final int COOLDOWN_INPUT = -DIRECTION_COOLDOWN;
    public static final int COOLDOWN_OUTPUT = DIRECTION_COOLDOWN;
    
    private FluidStack currentFluid;
    
    protected final Map<Parts, Section> sections = new EnumMap<>(Parts.class);
    
    public FluidPipeBlockEntity(BlockPos pos, BlockState state)
    {
        this(ModBlockEntities.FLUID_PIPE.get(), pos, state);
    }
    
    public FluidPipeBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state)
    {
        super(blockEntityType, pos, state);
        for(Parts part : Parts.values())
        {
            sections.put(part, new Section(SECTION_CAPACITY, part)
            {
                @Override
                protected void onContentsChanged()
                {
                    FluidPipeBlockEntity.this.syncToClient();
                }
            });
        }
        this.currentFluid = FluidStack.EMPTY;
    }
    
    public void syncToClient()
    {
        this.setChanged();
        BlockEntityUtil.sendUpdatePacket(this, this.saveWithFullMetadata());
    }
    
    public static void updateLinks(LevelAccessor level, BlockPos pos, BlockState state, FluidPipeBlockEntity blockEntity)
    {
        blockEntity.updateLinks(level, pos);
    }
    
    public void updateLinks(LevelAccessor level, BlockPos pos)
    {
        for(Direction direction : Direction.values())
        {
            this.links[direction.get3DDataValue()] = 0;
            BlockEntity fluidReceiver = level.getBlockEntity(pos.relative(direction));
            if(fluidReceiver != null)
            {
                IFluidHandler fluidHandler = fluidReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).orElse(null);
                if(fluidHandler != null)
                {
                    this.links[direction.get3DDataValue()] = 1;
                }
            }
            if(this.links[direction.get3DDataValue()] == 0)
            {
                this.sections.get(Parts.fromFacing(direction)).drain(500, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, FluidPipeBlockEntity blockEntity)
    {
        blockEntity.doTick();
    }
    
    public void doTick()
    {
        if(!this.level.isClientSide)
        {
            if(currentFluid != null)
            {
                this.updateLinks(this.level, this.worldPosition);
    
                int totalFluid = 0;
                boolean canOutput = false;
    
                for(Parts part : Parts.values())
                {
                    Section section = sections.get(part);
                    section.currentTime = (section.currentTime + 1) % TRANSFER_DELAY;
                    totalFluid += section.getFluidAmount();
                    if(section.getCurrentFlowDirection().canOutput())
                    {
                        canOutput = true;
                    }
                }
    
                if(totalFluid == 0)
                {
                    setFluid(FluidStack.EMPTY);
                }
                else
                {
                    if(canOutput)
                    {
                        moveFromPipe();
                    }
                    moveFromCenter();
                    moveToCenter();
                }
    
                for(Parts part : Parts.values())
                {
                    Section section = sections.get(part);
                    if(section.ticksInDirection > 0)
                    {
                        section.ticksInDirection--;
                    }
                    else if(section.ticksInDirection < 0)
                    {
                        section.ticksInDirection++;
                    }
                }
            }
    
            boolean send = false;
    
            for(Parts part : Parts.values())
            {
                Section section = sections.get(part);
                if(section.getFluidAmount() != section.lastTickAmount)
                {
                    send = true;
                    break;
                }
                else
                {
                    FlowDirection should = FlowDirection.get(section.ticksInDirection);
                    if(section.lastFlowDirection != should)
                    {
                        send = true;
                        break;
                    }
                }
            }
    
            this.syncToClient();
    
            if(send)
            {
                this.syncToClient();
            }
        }
    }
    
    protected void moveFromPipe()
    {
        for(Parts part : Parts.FACES)
        {
            Section section = sections.get(part);
            if(section.getCurrentFlowDirection().canOutput())
            {
                int maxDrain = section.drainInternal(TRANSFER_RATE, false);
                if(maxDrain <= 0)
                {
                    continue;
                }
                SidePriorities sidePriorities = new SidePriorities(currentFluid);
                sidePriorities.disallowAllExcept(part.face);
                if(sidePriorities.getOrder().size() == 1)
                {
                    FluidStack fluidToPush = new FluidStack(currentFluid, maxDrain);
                    
                    if(part != Parts.CENTER)
                    {
                        if(links[part.getIndex()] == 0)
                        {
                            continue;
                        }
                    }
                    
                    if(fluidToPush.getAmount() > 0)
                    {
                        int filled = this.sections.get(part).fill(fluidToPush, IFluidHandler.FluidAction.EXECUTE);
                        if(filled > 0)
                        {
                            section.drainInternal(filled, true);
                            section.ticksInDirection = COOLDOWN_OUTPUT;
                        }
                    }
                }
            }
        }
    }
    
    protected void moveFromCenter()
    {
        Section center = sections.get(Parts.CENTER);
        int totalAvailable = center.getMaxDrained();
        if(totalAvailable < 1)
        {
            return;
        }
        
        Set<Direction> realDirections = EnumSet.noneOf(Direction.class);
        
        for(Direction direction : Direction.values())
        {
            Section section = sections.get(Parts.fromFacing(direction));
            if(!section.getCurrentFlowDirection().canOutput())
            {
                continue;
            }
            if(section.getMaxFilled() > 0 && links[direction.get3DDataValue()] != 0)
            {
                realDirections.add(direction);
            }
        }
        
        if(realDirections.size() > 0)
        {
            SidePriorities sidePriorities = new SidePriorities(currentFluid);
            sidePriorities.disallowAllExcept(realDirections);
            
            EnumSet<Direction> set = sidePriorities.getOrder();
            
            List<Direction> random = new ArrayList<>(set);
            Collections.shuffle(random);
            
            float min = Math.min(TRANSFER_RATE * realDirections.size(), totalAvailable) / (float) TRANSFER_RATE / realDirections.size();
            for(Direction direction : random)
            {
                Section section = sections.get(Parts.fromFacing(direction));
                int available = section.fill(TRANSFER_RATE, false);
                int amountToPush = (int)(available * min);
                if(amountToPush < 1)
                {
                    amountToPush++;
                }
                
                amountToPush = center.drainInternal(amountToPush, false);
                if(amountToPush > 0)
                {
                    int filled = section.fill(amountToPush, true);
                    if(filled > 0)
                    {
                        center.drainInternal(filled, true);
                        section.ticksInDirection = COOLDOWN_OUTPUT;
                    }
                }
            }
        }
    }
    
    protected void moveToCenter()
    {
        int transferInCount = 0;
        Section center = sections.get(Parts.CENTER);
        int spaceAvailable = SECTION_CAPACITY - center.getFluidAmount();
        if(spaceAvailable <= 0 || center.getMaxFilled() <= 0)
        {
            return;
        }
        
        List<Parts> faces = new ArrayList<>();
        Collections.addAll(faces, Parts.FACES);
        Collections.shuffle(faces);
        
        int[] inputPerTick = new int[6];
        for(Parts part : faces)
        {
            Section section = sections.get(part);
            inputPerTick[part.getIndex()] = 0;
            if(section.getCurrentFlowDirection().canInput())
            {
                inputPerTick[part.getIndex()] = section.drainInternal(TRANSFER_RATE, false);
                if(inputPerTick[part.getIndex()] > 0)
                {
                    transferInCount++;
                }
            }
        }
        
        int[] fluidLeavingSide = new int[6];
        
        int left = Math.min(TRANSFER_RATE, spaceAvailable);
        float min = Math.min(TRANSFER_RATE * transferInCount, spaceAvailable) / (float) TRANSFER_RATE / transferInCount;
        for(Parts part : Parts.values())
        {
            Section section = sections.get(part);
            int i = part.getIndex();
            if(inputPerTick[i] > 0)
            {
                int amountToDrain = (int)(inputPerTick[i] * min);
                if(amountToDrain < 1)
                {
                    amountToDrain++;
                }
                if(amountToDrain > left)
                {
                    amountToDrain = left;
                }
                int amountToPush = section.drainInternal(amountToDrain, false);
                if(amountToPush > 0)
                {
                    fluidLeavingSide[i] = amountToPush;
                    left -= amountToPush;
                }
            }
        }
        
        int[] fluidEnteringCentre = Arrays.copyOf(fluidLeavingSide, 6);
        for(Parts part : Parts.values())
        {
            Section section = sections.get(part);
            int i = part.getIndex();
            int leaving = fluidLeavingSide[i];
            if(leaving > 0)
            {
                int actuallyDrained = section.drainInternal(leaving, true);
                if(actuallyDrained != leaving)
                {
                    throw new IllegalStateException("WHYYY");
                }
                if(actuallyDrained > 0)
                {
                    section.ticksInDirection = COOLDOWN_INPUT;
                }
                int entering = fluidEnteringCentre[i];
                if(entering > 0)
                {
                    int actuallyFilled = center.fill(entering, true);
                    if(actuallyFilled != entering)
                    {
                        throw new IllegalStateException("WHYYY");
                    }
                }
            }
        }
    }
    
    private void setFluid(FluidStack fluid)
    {
        currentFluid = fluid;
        for(Section section : sections.values())
        {
            section.incoming = new int[SECTION_CAPACITY];
            section.currentTime = 0;
            section.ticksInDirection = 0;
        }
    }
    
    public int[] getLinks()
    {
        return this.links;
    }
    
    public int getLink(int index)
    {
        return this.links[index];
    }
    
    public boolean getLinkBoolean(int index)
    {
        return this.links[index] != 0;
    }
    
    public int getFluidLevel()
    {
        int level = 0;
        for(Section section : sections.values())
        {
            level += section.getFluidAmount();
        }
        return level;
    }
    
    public int getFluidLevelCenter()
    {
        return sections.get(Parts.CENTER).getFluidAmount();
    }
    
    public int getFluidLevelSide(int side)
    {
        return sections.get(Parts.fromFacing(Direction.from3DDataValue(side))).getFluidAmount();
    }
    
    public FluidStack getFluidStack(int side)
    {
        return sections.get(Parts.fromFacing(Direction.from3DDataValue(side))).getFluid();
    }
    
    @Override
    public void load(CompoundTag compound)
    {
        super.load(compound);
        
        for(Parts part : Parts.values())
        {
            sections.put(part, new Section(SECTION_CAPACITY, part));
        }
        if(compound.contains("Fluid"))
        {
            setFluid(FluidStack.loadFluidStackFromNBT(compound.getCompound("Fluid")));
        }
        else
        {
            setFluid(FluidStack.EMPTY);
        }
        
        for(Parts part : Parts.values())
        {
            int direction = part.getIndex();
            if(compound.contains("tank[" + direction + "]"))
            {
                CompoundTag tag = compound.getCompound("tank[" + direction + "]");
                if(true)//if(tag.contains("FluidName"))
                {
                    FluidStack stack = FluidStack.loadFluidStackFromNBT(tag);
                    if(currentFluid == null)
                    {
                        setFluid(stack);
                    }
                    if(stack != null && stack.isFluidEqual(currentFluid))
                    {
                        sections.get(part).readFromNBT(tag);
                    }
                }
                else
                {
                    sections.get(part).readFromNBT(tag);
                }
            }
        }
        
        int[] aint = compound.getIntArray("Links");
        System.arraycopy(aint, 0, this.links, 0, aint.length);
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound)
    {
        super.saveAdditional(compound);
        
        if(currentFluid != null)
        {
            System.out.println("heya");
            CompoundTag fluidTag = new CompoundTag();
            currentFluid.writeToNBT(fluidTag);
            compound.put("Fluid", fluidTag);
    
            this.writeTanks(compound);
        }
        
        compound.putIntArray("Links", this.links);
    }
    
    @Override
    public void writeTanks(CompoundTag compound)
    {
        for(Parts part : Parts.values())
        {
            int direction = part.getIndex();
            CompoundTag tagTankSection = new CompoundTag();
            sections.get(part).writeToNBT(tagTankSection);
            compound.put("tank[" + direction + "]", tagTankSection);
        }
    }
    
    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }
    
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        this.load(pkt.getTag());
    }
    
    @Override
    public boolean areTanksEmpty()
    {
        return false;
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction facing)
    {
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return LazyOptional.of(() -> this.sections.get(Parts.fromFacing(facing))).cast();
        }
        return super.getCapability(cap, facing);
    }
    
    protected class Section extends FluidTank
    {
        public Section(int capacity, Parts part)
        {
            super(capacity);
            this.pipePart = part;
        }
    
        //Null means center
        Parts pipePart;
        
        int currentTime = 0;
        int lastTickAmount = 0;
        int ticksInDirection = 0;
        int incomingTotalCache = 0;
        int[] incoming = new int[1];
        FlowDirection lastFlowDirection;
        
        @Override
        public FluidTank readFromNBT(CompoundTag nbt)
        {
            this.lastTickAmount = nbt.getInt("LastTickAmount");
            this.ticksInDirection = nbt.getInt("TicksInDirection");
    
            incomingTotalCache = 0;
            for(int i = 0; i < incoming.length; ++i)
            {
                incomingTotalCache += incoming[i] = nbt.getInt("in[" + i + "]");
            }
            
            return super.readFromNBT(nbt);
        }
    
        @Override
        public CompoundTag writeToNBT(CompoundTag nbt)
        {
            nbt.putInt("LastTickAmount", this.lastTickAmount);
            nbt.putInt("TicksInDirection", this.ticksInDirection);
            
            for(int i = 0; i < incoming.length; ++i)
            {
                nbt.putInt("in[" + i + "]", incoming[i]);
            }
            
            return super.writeToNBT(nbt);
        }
        
        public int getMaxFilled()
        {
            int availableTotal = capacity - fluid.getAmount();
            int availableThisTick = TRANSFER_RATE - incoming[currentTime];
            return Math.min(availableTotal, availableThisTick);
        }
        
        public int getMaxDrained()
        {
            return Math.min(fluid.getAmount() - incomingTotalCache, TRANSFER_RATE);
        }
        
        public int fill(int maxFill, boolean doFill)
        {
            int amountToFill = Math.min(getMaxFilled(), maxFill);
            if(amountToFill <= 0)
            {
                return 0;
            }
            
            if(doFill)
            {
                incoming[currentTime] += amountToFill;
                incomingTotalCache += amountToFill;
                fluid.setAmount(fluid.getAmount() + amountToFill);
            }
            return amountToFill;
        }
        
        public int drainInternal(int maxDrain, boolean doDrain)
        {
            maxDrain = Math.min(maxDrain, getMaxDrained());
            if(maxDrain <= 0)
            {
                return 0;
            }
            else
            {
                if(doDrain)
                {
                    fluid.setAmount(fluid.getAmount() - maxDrain);
                }
                return maxDrain;
            }
        }
        
        public FlowDirection getCurrentFlowDirection()
        {
            return ticksInDirection == 0 ? FlowDirection.NONE : ticksInDirection < 0 ? FlowDirection.IN : FlowDirection.OUT;
        }
    
        @Override
        public int fill(FluidStack resource, FluidAction action)
        {
            return super.fill(resource, action);
            /*if(!getCurrentFlowDirection().canInput() || !pipe.isConnected(part.face) || resource == null)
            {
                return 0;
            }
            resource = resource.copy();*/
            //PipeEventFluid.TryInsert tryInsert = new PipeEventFluid.TryInsert(pipe.getHolder(), PipeFlowFluids.this, part.face, resource);
            //pipe.getHolder().fireEvent(tryInsert);
            /*if(tryInsert.isCanceled())
            {
                return 0;
            }*/
    
            /*if(currentFluid == null || currentFluid.isFluidEqual(resource))
            {
                boolean doFill = action == FluidAction.EXECUTE;
                if(doFill)
                {
                    if(currentFluid == null)
                    {
                        setFluid(resource.copy());
                    }
                }
                int filled = fill(resource.getAmount(), doFill);
                if(filled > 0 && doFill)
                {
                    ticksInDirection = COOLDOWN_INPUT;
                }
                return filled;
            }
            return 0;*/
        }
    }
    
    public class SidePriorities
    {
        public final FluidStack fluid;
        
        private final int[] priority = new int[6];
        private final EnumSet<Direction> allowed = EnumSet.allOf(Direction.class);
        
        public SidePriorities(FluidStack fluid)
        {
            this.fluid = fluid;
        }
        
        public boolean isAllowed(Direction side)
        {
            return allowed.contains(side);
        }
        
        public void disallowAllExcept(Direction side)
        {
            if(allowed.contains(side))
            {
                allowed.clear();;
                allowed.add(side);
            }
            else
            {
                allowed.clear();
            }
        }
        
        public void disallowAllExcept(Direction... sides)
        {
            switch(sides.length)
            {
                case 0:
                    allowed.clear();
                    break;
                case 1:
                    disallowAllExcept(sides[0]);
                    break;
                case 2:
                    allowed.retainAll(EnumSet.of(sides[0], sides[1]));
                    break;
                case 3:
                    allowed.retainAll(EnumSet.of(sides[0], sides[1], sides[2]));
                    break;
                case 4:
                    allowed.retainAll(EnumSet.of(sides[0], sides[1], sides[2], sides[3]));
                    break;
                default:
                    EnumSet<Direction> except = EnumSet.noneOf(Direction.class);
                    for(Direction face : sides)
                    {
                        except.add(face);
                    }
                    this.allowed.retainAll(except);
                    break;
            }
        }
        
        public void disallowAllExcept(Collection<Direction> sides)
        {
            allowed.retainAll(sides);
        }
        
        public EnumSet<Direction> getOrder()
        {
            if(allowed.isEmpty())
            {
                return EnumSet.noneOf(Direction.class);
            }
            if(allowed.size() == 1)
            {
                return allowed;
            }
            priority_search:
            {
                int val = priority[0];
                for(int i = 1; i < priority.length; i++)
                {
                    if(priority[i] != val)
                    {
                        break priority_search;
                    }
                }
                return allowed;
            }
            int[] ordered = Arrays.copyOf(priority, 6);
            Arrays.sort(ordered);
            int last = 0;
            for(int i = 0; i < 6; i++)
            {
                int current = ordered[i];
                if(i != 0 && current == last)
                {
                    continue;
                }
                last = current;
                EnumSet<Direction> set = EnumSet.noneOf(Direction.class);
                for(Direction face : Direction.values())
                {
                    if(allowed.contains(face))
                    {
                        if(priority[face.get3DDataValue()] == current)
                        {
                            set.add(face);
                        }
                    }
                }
                if(set.size() > 0)
                {
                    return set;
                }
            }
            return EnumSet.noneOf(Direction.class);
        }
    }
    
    public enum FlowDirection
    {
        IN(-1),
        NONE(0),
        OUT(1);
        
        int nbtValue;
        
        FlowDirection(int nbtValue)
        {
            this.nbtValue = nbtValue;
        }
        
        public boolean isInput()
        {
            return this == IN;
        }
        
        public boolean canInput()
        {
            return this != OUT;
        }
        
        public boolean isOutput()
        {
            return this == OUT;
        }
        
        public boolean canOutput()
        {
            return this != IN;
        }
        
        public static FlowDirection get(int flowDirection)
        {
            if(flowDirection == 0)
            {
                return NONE;
            }
            else if(flowDirection < 0)
            {
                return IN;
            }
            else
            {
                return OUT;
            }
        }
    }
}
