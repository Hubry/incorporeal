package quaternary.incorporeal.cygnus;

import com.google.common.base.Preconditions;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import quaternary.incorporeal.Incorporeal;

import java.util.Optional;

public class CygnusStack {
	public CygnusStack(int maxDepth) {
		this.maxDepth = maxDepth;
		clear();
	}
	
	private int maxDepth;
	private Object[] stack;
	private int cursor = 0;
	
	public boolean push(Object item) {
		Preconditions.checkNotNull(item);
		if(item.getClass() == Optional.class) throw new IllegalArgumentException("Can't push Optionals onto the stack - programmer error");
		
		if(stack.length == maxDepth) return false;
		
		stack[cursor] = item;
		cursor++;
		return true;
	}
	
	public Optional<Object> pop() {
		if(cursor == 0) return Optional.empty();
		
		Object removedObject = stack[cursor];
		stack[cursor] = null;
		cursor--;
		return Optional.of(removedObject);
	}
	
	public void popDestroy(int popDepth) {
		if(cursor < popDepth) {
			throw new IllegalStateException("Stack underflow using popDestroy - programmer error");
		}
		
		for(int i = 0; i < popDepth; i++) {
			stack[cursor] = null;
			cursor--;
		}
	}
	
	//Will still pop if the class doesn't match!!
	public <T> Optional<T> popMatching(Class<T> matchingClass) {
		return (Optional<T>) pop().filter(o -> o.getClass().equals(matchingClass));
	}
	
	public Optional<Object> peek() {
		return peek(0);
	}
	
	public Optional<Object> peek(int peekDepth) {
		if(cursor < peekDepth) return Optional.empty();
		return Optional.of(stack[cursor - peekDepth - 1]);
	}
	
	public <T> Optional<T> peekMatching(Class<T> matchingClass) {
		return peekMatching(matchingClass, 0);
	}
	
	public <T> Optional<T> peekMatching(Class<T> matchingClass, int peekDepth) {
		return (Optional<T>) peek(peekDepth).filter(o -> o.getClass().equals(matchingClass));
	}
	
	public int depth() {
		return cursor;
	}
	
	public boolean isEmpty() {
		return cursor == 0;
	}
	
	public boolean isFull() {
		return cursor == maxDepth;
	}
	
	public int maxDepth() {
		return maxDepth;
	}
	
	public void clear() {
		stack = new Object[maxDepth];
	}
	
	public CygnusStack copy() {
		CygnusStack other = new CygnusStack(maxDepth);
		//TODO make sure this doesn't push stuff backwards, lol.
		for(int i = 0; i < cursor; i++) {
			other.push(stack[i]);
		}
		return other;
	}
	
	//serializing!
	
	public NBTTagCompound toNBT() {
		NBTTagCompound ret = new NBTTagCompound();
		
		ret.setInteger("MaxDepth", maxDepth);
		
		NBTTagList nbtList = new NBTTagList();
		for(int i = 0; i < cursor; i++) {
			NBTTagCompound entry = new NBTTagCompound();
			entry.setInteger("Depth", i);
			Incorporeal.API.getCygnusSerializerRegistry().writeToNBT(entry, stack[i]);
			nbtList.appendTag(entry);
		}
		
		ret.setTag("Stack", nbtList);
		
		return ret;
	}
	
	public void fromNBT(NBTTagCompound nbt) {
		maxDepth = nbt.getInteger("MaxDepth");
		clear();
		
		NBTTagList nbtList = nbt.getTagList("Stack", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < nbtList.tagCount(); i++) {
			NBTTagCompound entry = nbtList.getCompoundTagAt(i);
			stack[i] = Incorporeal.API.getCygnusSerializerRegistry().readFromNBT(entry);
		}
	}
	
	public void toPacketBuffer(PacketBuffer buf) {
		buf.writeInt(maxDepth);
		buf.writeInt(cursor);
		for(int i = 0; i < cursor; i++) {
			Incorporeal.API.getCygnusSerializerRegistry().writeToPacketBuffer(buf, stack[i]);
		}
	}
	
	public void fromPacketBuffer(PacketBuffer buf) {
		maxDepth = buf.readInt();
		clear();
		
		int stackAmount = buf.readInt();
		for(int i = 0; i < stackAmount; i++) {
			stack[i] = Incorporeal.API.getCygnusSerializerRegistry().readFromPacketBuffer(buf);
		}
	}
}
