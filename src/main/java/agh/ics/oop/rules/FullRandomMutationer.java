package agh.ics.oop.rules;

public class FullRandomMutationer implements IRuleMutations{
	@Override
	public int mutate(int startingGene) {
		return (int) Math.floor(Math.random()*8);
	}
}
