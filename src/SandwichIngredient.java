
public class SandwichIngredient {
	public boolean ingredientMoveSwitch; //프로젝트 내에서 쓰이는 곳 없음
	public int ingredientOrder;//여기랑 GS 273, 279, 344 줄
	public int ingredientXPos, ingredientYPos;//X는 GS 345 줄, Y는 GS 346, 358 줄
	
	public void createSandwichIngredient(int inputIngredientNumber) { //GS 257
		ingredientOrder 	= inputIngredientNumber;
		ingredientXPos = 0;
		ingredientYPos = 0;
	}
}
