
public class SandwichIngredient {
	public boolean ingredientMoveSwitch;
	public int ingredientOrder;
	public int ingredientXPos, ingredientYPos;
	
	public void createSandwichIngredient(int inputIngredientNumber) {
		ingredientOrder 	= inputIngredientNumber;
		ingredientXPos = 0;
		ingredientYPos = 0;
	}
}
