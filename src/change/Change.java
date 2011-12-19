package change;

import java.util.LinkedList;

import swing.Slyum;

public class Change
{
	private static boolean block = false;
	private static int pointer = 0;
	
	private static boolean _hasChange = false;

	private static LinkedList<Changeable> stack = new LinkedList<Changeable>();

	private static LinkedList<Boolean> record = new LinkedList<Boolean>();
	
	private static boolean isRecord = false;

	private static void printStackState()
	{
		/*
		System.out.println("Etat de la pile");

		for (int i = 0; i < stack.size(); i++)
		
			System.out.println(i + " - " + record.get(i) + (pointer == i ? " <--" : ""));
		

		System.out.println("--------------");
		*/
	}

	public static void push(Changeable ch)
	{
		if (block)
			return;
		
		// Remove all elements positioned after index pointer.
		while (stack.size() > 1 && pointer < stack.size() - 1)
		{
			stack.removeLast();
			stack.removeLast();
			
			record.removeLast();
			record.removeLast();
		}

		stack.add(ch);
		record.add(isRecord);
		
		pointer = stack.size() - 1;

		printStackState();
		
		checkToolbarButtonState();
		
		setHasChange(true);
	}

	public static void redo()
	{
		if (pointer >= stack.size() - 1)
			return;

		final int increment = pointer % 2 == 0 ? 1 : 2;

		final boolean isBlocked = Change.isBlocked();
		setBlocked(true);
		stack.get(pointer += increment).restore();
		setBlocked(isBlocked);

		printStackState();
		
		checkToolbarButtonState();
		
		setHasChange(true);
		
		if (record.get(pointer))
			redo();
	}

	public static void undo()
	{
		if (pointer <= 0)
			return;
		
		final int decrement = pointer % 2 > 0 ? 1 : 2;

		final boolean isBlocked = Change.isBlocked();
		setBlocked(true);
		stack.get(pointer -= decrement).restore();
		setBlocked(isBlocked);

		printStackState();

		checkToolbarButtonState();
		
		setHasChange(true);
		
		if (record.get(pointer))
			
			undo();
	}
	
	/**
	 * Begin a record. A record merge all new pushes in a same group. When undo / redo is called, all
	 * pushes into a group will be undo / redo at the same time.
	 */
	public static void record()
	{
		isRecord = true;
	}
	
	/**
	 * Stop the current record. If no record is currently running this method have no effect.
	 */
	public static void stopRecord()
	{
		int size = stack.size();
		
		if (isRecord = false || size < 1)
			return;
		
		isRecord = false;

		int b = pointer;
		while (--b >= 0 && b < size-1 && record.get(b));
		
		record.set(b+1, false);
		record.set(pointer, false);
	}
	
	public static boolean isRecord()
	{
		return isRecord;
	}
	
	protected static void checkToolbarButtonState()
	{
		Slyum.setEnableRedoButtons(pointer < stack.size() - 1);
		Slyum.setEnableUndoButtons(pointer > 0);
	}

	public static void setBlocked(boolean blocked)
	{
		block = blocked;
	}
	
	public static boolean isBlocked()
	{
		return block;
	}
	
	public static void clear()
	{
		stack.clear();
		
		printStackState();
	}

	public static boolean hasChange()
	{
		return _hasChange;
	}
	
	public static void setHasChange(boolean changed)
	{
		_hasChange = changed;
		
		Slyum.setStarOnTitle(changed);
	}
	
	public static int getSize()
	{
		return stack.size();
	}
	
	public static Changeable getLast()
	{
		return stack.getLast();
	}
	
	public static void pop()
	{
		stack.removeLast();
		record.removeLast();
	}
	
}
