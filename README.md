# SubwayAlba
1. System.out.println(mouse.getMouseXPos()); \
이용 시 마우스 좌표 따기 좋음
2. exit 버튼으로 종료 안됨 - 구조적으로 문제가 있는듯, 삽질하다 실패 \
X 버튼 누르면 종료되는 부분 구현 어디있는지 찾으면 해결될 것 같은데, 못찾음 \
그 외 일단 mainScreen 건드릴 것 없음
3. this.getWidth()/2 - 160 : 정확히 x축 가운데에 두고 싶을때 \
4. 주석처리된 숫자들은 해당 선언이 사용된 라인을 의미하는 것이었음. 즉 지워도됨 \
5. resultscreen에 서현이가 쓰레드 넣을 것임, try again 구현 가능 \
6. 각 버거 재료들은 폴더 또는 파일 이름별로 나누는 것이 편할 것.
크게 3종류 - example, menu, ingredient \
example은 예시 \
menu는 만들어지고있는 샌드위치 \
ingredient는 재료 보드에서 선택할 재료 \
순서 : 빵 - 치즈 - 채소(양상추 토마토 양파) - 채소2 (나머지 5개) - 고기 4종류 - 소스 - 포장 \ 
* 각 단계에서 채소들 빼고는 1개만 할당되도록 함
* 각 단계의 인덱싱이 지켜져야함(파일 이름 순서)
* 단계의 순서가 바뀌어서는 안됨

----지연-----
* ChooseDifficulty 완료, 글씨체 알아보고 바꿀것임 너무 구려 
* GameManagement부터 하면 되는데, (displayMenu-사진 바꾸고 사각형 추가하고 상황에 따라 수정 등등), 지연이가 마저 displayMenu와 좌표 구할 것
* exit아직 수정 못함