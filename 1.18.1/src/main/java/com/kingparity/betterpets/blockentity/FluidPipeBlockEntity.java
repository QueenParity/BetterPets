package com.kingparity.betterpets.blockentity;

import com.google.common.collect.EnumMultiset;
import com.google.common.collect.Multiset;
import com.kingparity.betterpets.init.ModBlockEntities;
import com.kingparity.betterpets.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
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
    
    public static final int SECTION_CAPACITY = 250;
    public static final int TRANSFER_RATE = 10;
    public static final int MAX_TRANSFER_DELAY = 12;
    public static final int INPUT_TICKS = 60;
    public static final int OUTPUT_TICKS = 80;
    
    //private FluidStack currentFluid;
    
    private int transferDelay = MAX_TRANSFER_DELAY;
    private int transferRate, capacity;
    
    public final Map<Parts, Section> sections = new EnumMap<>(Parts.class);
    
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
                    //FluidPipeBlockEntity.this.syncToClient();
                }
            });
        }
        //this.currentFluid = FluidStack.EMPTY;
        
        capacity = 25 * TRANSFER_RATE;
        transferRate = 1 * TRANSFER_RATE;
        transferDelay = Mth.clamp(Math.round(16F / (transferRate / TRANSFER_RATE)), 1, MAX_TRANSFER_DELAY);
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
            //((ServerLevel)level).removeEntity(level.getNearestEntity(ArmorStand.class, TargetingConditions.forNonCombat().range(16.0D).ignoreLineOfSight().ignoreInvisibilityTesting().selector((p_30636_) -> p_30636_ instanceof AbstractHorse && ((AbstractHorse)p_30636_).isBred()), this, ));
            /*ServerLevel serverLevel = ((ServerLevel)level);
            List<? extends ArmorStand> closest = serverLevel.getEntities(EntityType.ARMOR_STAND, (entity) -> entity.getName().getString().equals("Yooo"));
            if(closest.size() != 0)
            {
                Entity gottenEntity = closest.get(0);
                if(gottenEntity != null)
                {
                    //((ServerLevel)level).data
                    gottenEntity.getEntityData().assignValues();
                    serverLevel.removeEntity(gottenEntity);
                }
                else
                {
                    System.out.println("aww");
                }
            }
            else
            {
                System.out.println("aww");
            }
            CompoundTag entityCompound = new CompoundTag();*/
            //entityCompound.putString("CustomName", "Yooo");
            /*entityCompound.putBoolean("CustomNameVisible", true);
            entityCompound.putBoolean("NoGravity", true);
            
            ListTag listTag1 = new ListTag();
            
            listTag1.add(FloatTag.valueOf(0.0F));
            listTag1.add(FloatTag.valueOf(0.0F));
            
            entityCompound.put("Rotation", listTag1);
            EntityType.ARMOR_STAND.spawn((ServerLevel)level, entityCompound, new TextComponent("Yooo"), ((ServerLevel) level).getRandomPlayer(), worldPosition.relative(Direction.UP), MobSpawnType.COMMAND, false, false);*/
            FluidStack stack = FluidStack.EMPTY;
            for(Section section : sections.values())
            {
                if(section.getFluid().getFluid() != Fluids.EMPTY)
                {
                    stack = section.getFluid();
                    break;
                }
                //currentFluid = sections.get(Parts.CENTER).getFluid();//this.setFluid(sections.get(Parts.CENTER).getFluid());//currentFluid = sections.get(Parts.CENTER)
            }
    
            if(stack != FluidStack.EMPTY)
            {
                this.updateLinks(this.level, this.worldPosition);
    
                int newTime = (int)(this.level.getGameTime() % transferDelay);
                boolean[] canOutput = compute(newTime > 0 && newTime < transferDelay ? newTime : 0);
                
                if(stack != FluidStack.EMPTY)
                {
                    if(canOutput[1])
                    {
                        if(canOutput[0])
                        {
                            moveFromPipe();
                        }
                        moveFromCenter();
                        moveToCenter();
                    }
                }
            }
            else
            {
                for(Parts part : Parts.values())
                {
                    Section section = sections.get(part);
                    if(section.lastFlowDirection == FlowDirection.IN)
                    {
                        if(section.inputTicks > 0)
                        {
                            section.inputTicks--;
                        }
                        else
                        {
                            section.lastFlowDirection = FlowDirection.NONE;
                        }
                    }
                }
            }
    
            /*boolean send = false;
    
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
            
            
            if(send)
            {
            
            }*/
            this.setChanged();
            //this.syncToClient();
    
            /*if(send)
            {
                this.syncToClient();
            }*/
            
            if(this.level.dayTime() % 1 == 0)
            {
                this.syncToClient();
            }
            if(this.level.random.nextFloat() < 0.1)
            {
                BlockEntityUtil.sendUpdatePacket(this, this.saveWithFullMetadata());
            }
        }
    }
    
    protected boolean[] compute(int newTime)
    {
        int totalFluid = 0;
        boolean[] canOutput = new boolean[]{false, true};
        
        for(Parts part : Parts.values())
        {
            Section section = sections.get(part);
            totalFluid += section.getFluidAmount();
            section.setTime(newTime);
            section.advanceForMovement();
            
            if(part == Parts.CENTER)
            {
                continue;
            }
            if(section.lastFlowDirection.isInput())
            {
                section.inputTicks--;
                if(section.inputTicks <= 0)
                {
                    section.lastFlowDirection = FlowDirection.NONE;
                }
                continue;
            }
            if(links[part.face.get3DDataValue()] == 0)
            {
                section.lastFlowDirection = FlowDirection.NONE;
                continue;
            }
            if(section.outputTicks <= 0)
            {
                section.lastFlowDirection = FlowDirection.NONE;
                section.outputTicks = OUTPUT_TICKS;
                continue;
            }
        
            if(links[part.face.get3DDataValue()] != 0)
            {
                section.lastFlowDirection = FlowDirection.OUT;
                canOutput[0] = true;
            }
        }
        
        if(totalFluid == 0)
        {
            canOutput[1] = false;
        }
        
        return canOutput;
    }
    
    protected void moveFromPipe()
    {
        for(Parts part : Parts.FACES)
        {
            Section section = sections.get(part);
            if(section.lastFlowDirection.isOutput())
            {
                int maxDrain = section.drain(transferRate, IFluidHandler.FluidAction.SIMULATE).getAmount();
                if(maxDrain <= 0)
                {
                    continue;
                }
                BlockEntity blockEntity = this.level.getBlockEntity(this.worldPosition.relative(part.face));
                IFluidHandler fluidHandler = blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, part.face.getOpposite()).orElse(null);
                
                if(fluidHandler == null)
                {
                    continue;
                }

                if(links[part.getIndex()] == 0)
                {
                    continue;
                }

                FluidStack fluidToPush = new FluidStack(section.getFluid(), maxDrain);
                
                if(fluidToPush.getAmount() > 0)
                {
                    int filled = fluidHandler.fill(fluidToPush, IFluidHandler.FluidAction.EXECUTE);
                    
                    if(filled > 0)
                    {
                        if(blockEntity instanceof FluidPipeBlockEntity)
                        {
                            FluidPipeBlockEntity fluidPipeBlockEntity = (FluidPipeBlockEntity)blockEntity;
                            fluidPipeBlockEntity.sections.get(Parts.fromFacing(part.face.getOpposite())).lastFlowDirection = FlowDirection.IN;
                            fluidPipeBlockEntity.sections.get(Parts.fromFacing(part.face.getOpposite())).inputTicks = INPUT_TICKS;
                        }
                        else
                        {
                            System.out.println("nuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu");
                        }
                        section.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                    }
                    else
                    {
                        section.outputTicks--;
                    }
                }
            }
        }
    }
    
    protected void moveFromCenter()
    {
        Section center = sections.get(Parts.CENTER);
        int pushAmount = center.getFluidAmount();
        int totalAvailable = center.getMaxDrained();
        if(totalAvailable < 1 || pushAmount < 1)
        {
            System.out.println(worldPosition.getX() + ", " + worldPosition.getY() + ", " + worldPosition.getZ() + " D:");
            return;
        }
        else
        {
            System.out.println(worldPosition.getX() + ", " + worldPosition.getY() + ", " + worldPosition.getZ() + " :D");
        }
        
        Multiset<Direction> realDirections = EnumMultiset.create(Direction.class);
        
        for(Direction direction : Direction.values())
        {
            Section section = sections.get(Parts.fromFacing(direction));
            if(links[direction.get3DDataValue()] != 0 && section.lastFlowDirection.isOutput())
            {
                realDirections.add(direction);
            }
            else
            {
                System.out.println("oh");
            }
        }
        
        if(realDirections.size() > 0)
        {
            System.out.println("???");
            
            float min = Math.min(transferRate * realDirections.size(), totalAvailable) / (float) transferRate / realDirections.size();
            for(Direction direction : realDirections.elementSet())
            {
                Fluid currentFluid = center.getFluid().getFluid();
                
                Section section = sections.get(Parts.fromFacing(direction));
                int available = section.fill(new FluidStack(currentFluid, transferRate), IFluidHandler.FluidAction.SIMULATE);
                int amountToPush = (int)(available * min * realDirections.count(direction));
                if(amountToPush < 1)
                {
                    amountToPush++;
                }
                
                amountToPush = center.drain(new FluidStack(currentFluid, amountToPush), IFluidHandler.FluidAction.SIMULATE).getAmount();
                if(amountToPush > 0)
                {
                    int filled = section.fill(new FluidStack(currentFluid, amountToPush), IFluidHandler.FluidAction.EXECUTE);
                    center.drain(new FluidStack(currentFluid, filled), IFluidHandler.FluidAction.EXECUTE);
                    /*if(filled > 0)
                    {
                        center.drain(new FluidStack(currentFluid, filled), IFluidHandler.FluidAction.EXECUTE);
                        System.out.println("sanity");
                    }*/
                }
                else
                {
                    System.out.println("awwwwwww");
                }
            }
        }
        else
        {
            System.out.println("?");
        }
    }
    
    protected void moveToCenter()
    {
        int transferInCount = 0;
        Section center = sections.get(Parts.CENTER);
        int spaceAvailable = capacity - center.getFluidAmount();
        
        List<Parts> faces = new ArrayList<>();
        Collections.addAll(faces, Parts.FACES);
        //Collections.shuffle(faces);
        
        int[] inputPerTick = new int[6];
        for(Parts part : faces)
        {
            Section section = sections.get(part);
            inputPerTick[part.getIndex()] = 0;
            if(section.lastFlowDirection.canInput())
            {
                inputPerTick[part.getIndex()] = section.drain(new FluidStack(section.getFluid(), transferRate), IFluidHandler.FluidAction.SIMULATE).getAmount();
                if(inputPerTick[part.getIndex()] > 0)
                {
                    transferInCount++;
                }
            }
        }
        
        float min = Math.min(transferRate * transferInCount, spaceAvailable) / (float) transferRate / transferInCount;
        
        for(Parts part : faces)
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
                
                Fluid currentFluid = section.getFluid().getFluid();
                int amountToPush = section.drain(new FluidStack(currentFluid, amountToDrain), IFluidHandler.FluidAction.SIMULATE).getAmount();
                if(amountToPush > 0)
                {
                    int filled = center.fill(new FluidStack(currentFluid, amountToPush), IFluidHandler.FluidAction.EXECUTE);
                    section.drain(new FluidStack(currentFluid, filled), IFluidHandler.FluidAction.EXECUTE);
                }
                System.out.println(worldPosition.getX() + ", " + worldPosition.getY() + ", " + worldPosition.getZ() + " aaa "  + part.face.getName() + " " + currentFluid.getRegistryName());
            }
        }
    }
    
    /*protected void setFluid(FluidStack fluid)
    {
        currentFluid = fluid;
        for(Section section : sections.values())
        {
            section.setValidator(stack -> stack.getFluid() == fluid.getFluid());
            section.incoming = new int[TRANSFER_DELAY];
            section.currentTime = 0;
            section.ticksInDirection = 0;
        }
    }*/
    
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
        if(side == -1)
        {
            return sections.get(Parts.fromFacing(null)).getFluid();
        }
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
        
        for(Parts part : Parts.values())
        {
            int direction = part.getIndex();
            if(compound.contains("tank[" + direction + "]"))
            {
                CompoundTag tag = compound.getCompound("tank[" + direction + "]");
                if(true)//tag.contains("FluidName"))
                {
                    FluidStack stack = FluidStack.loadFluidStackFromNBT(tag);
                    if(stack != null)
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
        
        this.writeTanks(compound);
        
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
    
    public class Section extends FluidTank
    {
        public Section(int capacity, Parts part)
        {
            super(capacity);
            this.pipePart = part;
        }
        
        public final Parts pipePart;
        public int lastTickAmount = 0;
        public FlowDirection lastFlowDirection = FlowDirection.NONE;
        public int currentTime = 0;
        public int[] incoming = new int[MAX_TRANSFER_DELAY];
        public int outputTicks = OUTPUT_TICKS;
        public int inputTicks = 0;
        
        @Override
        public FluidTank readFromNBT(CompoundTag nbt)
        {
            this.lastTickAmount = nbt.getInt("LastTickAmount");
            this.outputTicks = nbt.getInt("OutputTicks");
            this.inputTicks = nbt.getInt("InputTicks");
            this.lastFlowDirection = FlowDirection.get(nbt.getInt("FlowDirection"));
            this.currentTime = nbt.getInt("CurrentTime");
            
            for(int i = 0; i < transferDelay; ++i)
            {
                incoming[i] = nbt.getInt("in[" + i + "]");
            }
            
            return super.readFromNBT(nbt);
        }
        
        @Override
        public CompoundTag writeToNBT(CompoundTag nbt)
        {
            nbt.putInt("LastTickAmount", this.lastTickAmount);
            nbt.putInt("OutputTicks", this.outputTicks);
            nbt.putInt("InputTicks", this.inputTicks);
            nbt.putInt("FluidDirection", this.lastFlowDirection.nbtValue);
            nbt.putInt("CurrentTime", this.currentTime);
            
            for(int i = 0; i < transferDelay; ++i)
            {
                nbt.putInt("in[" + i + "]", incoming[i]);
            }
            
            return super.writeToNBT(nbt);
        }
        
        public int getMaxFilled()
        {
            int availableTotal = capacity - fluid.getAmount();
            int availableThisTick = transferRate - incoming[currentTime];
            return Math.min(availableTotal, availableThisTick);
        }
        
        public int getMaxDrained()
        {
            int all = this.getFluidAmount();
            for(int slot : incoming)
            {
                all -= slot;
            }
            return all;
        }
        
        public void advanceForMovement()
        {
            incoming[currentTime] = 0;
        }
        
        public void setTime(int newTime)
        {
            currentTime = newTime;
        }
        
        public void reset()
        {
            this.setFluid(FluidStack.EMPTY);
            incoming = new int[MAX_TRANSFER_DELAY];
        }
        
        @Override
        public int fill(FluidStack resource, FluidAction action)
        {
            int maxFill = resource.getAmount();
            int amountToFill = Math.min(getMaxFilled(), maxFill);
            if(amountToFill <= 0)
            {
                return 0;
            }
            
            if(action == FluidAction.EXECUTE)
            {
                incoming[currentTime] += amountToFill;
                if(super.fill(new FluidStack(resource, amountToFill), action) != amountToFill)
                {
                    System.out.println("oh no @fill(FluidStack, FluidAction) in FluidPipeBlockEntity");
                }
            }
            return amountToFill;
        }
        
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action)
        {
            int maxDrain = resource.getAmount();
            int maxToDrain = getMaxDrained();
            if(maxToDrain > maxDrain)
            {
                maxToDrain = maxDrain;
            }
            if(maxToDrain > transferRate)
            {
                maxToDrain = transferRate;
            }
            if(maxToDrain <= 0)
            {
                return FluidStack.EMPTY;
            }
            else
            {
                if(action == FluidAction.EXECUTE)
                {
                    if(super.drain(new FluidStack(resource, maxToDrain), action).getAmount() != maxToDrain)
                    {
                        System.out.println("oh no @drain(FluidStack, FluidAction) in FluidPipeBlockEntity");
                    }
                }
                return new FluidStack(resource, maxToDrain);
            }
        }
    }
    
    /*public class SidePriorities
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
    }*/
    
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
