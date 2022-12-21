package agh.ics.oop.rules;

public class FullRandomMutationer implements IRuleMutations{
	@Override
	public int mutate() {
		return (int) Math.floor(Math.random()*8);
	}
}
