# SWExpertAcademy_MockTest_Java_5656

## SW Expert Academy  5656. [모의 SW 역량테스트] 벽돌 깨기

### 1. 문제설명

출처: https://swexpertacademy.com/main/code/problem/problemDetail.do?contestProbId=AWXRQm6qfL0DFAUo

input 정수 `N`, `W`, `H`가 들어온다. 각각은 떨어뜨릴 구슬의 개수, 이후에 들어올 벽돌정보 배열의 너비와 높이를 뜻한다. 이어서 `H`줄의 벽돌 정보가 들어온다. 벽돌의 정보는 `0`은 벽돌이 없음을 뜻하며 `0`아닌 숫자들은 벽돌이 깨질 때 퍼져나가는 범위를 뜻한다. `N`개의 구슬을 특정 열에 떨어뜨리면 해당 열에서 가장 높은 벽돌과 부딛히며 벽돌에 적힌 값을 `v`라 할때, `v-1`만큼 상하좌우의 벽돌도 함께 깨진다.

해당 규칙에 따라 벽돌이 깨진다고할때 `N`개의 구슬을 떨어 뜨려 남을 벽돌의 최솟값을 찾아 출력하는 문제이다.

[제약 사항]
* 1 ≤ N ≤ 4
* 2 ≤ W ≤ 12
* 2 ≤ H ≤ 15

[입력]

> 가장 첫 줄에는 총 테스트 케이스의 개수 `T` 가 주어지고,
> 그 다음 줄부터 `T` 개의 테스트 케이스가 주어진다.
> 각 테스트 케이스의 첫 번째 줄에는 `N`, `W`, `H` 가 순서대로 공백을 사이에 두고 주어지고,
> 다음 `H` 줄에 걸쳐 벽돌들의 정보가 `1` 줄에 `W` 개씩 주어진다.

[출력]

> 출력은 `#t` 를 찍고 한 칸 띄운 다음 정답을 출력한다.
> (`t` 는 테스트 케이스의 번호를 의미하며 `1` 부터 시작한다)


### 2. 풀이

`N`개의 구슬을 떨어뜨리는 방법을 dfs방식으로 모든경우를 구하여 시도하여주었고, 벽돌이 깨지는 방식은 구슬이 떨어지는 `drop`함수, 벽돌이 깨지는 `breakBlock`함수, 하나의 구슬을 떨어뜨린 후 남은 블럭들이 아래도 내려가도록 하는 `down`함수를 만들어 `N`회 반복해서 수행해주었다. 마지막 남은 블럭들은 map을 탐색하며 카운트하였고 전역변수로 둔 `min`과 비교하여 값을 갱신해주었다. `min`값은 `H*W`의 값으로 초기화 하였으며 모든 경우를 다 조사하여도 값이 변하지 않는다면 블럭이 애초에 존재하지 않아 바뀐것이 없다는 뜻이기 때문에 `0`을 출력해주었다.

* drop

dfs를 통하여 `N`개의 위치가 정해졌을때 수행되는 함수이다. 배열 `answer`에는 떨어뜨릴 구슬의 열이 저장되어있으며 순차적으로 떨어뜨려 벽돌을 깨는 행위인 `breakBlock`, 깨진 벽돌을 아래로 내리는 행위은 `down`이 반복된다.

```java
private static void drop(int[] answer) {
  initTemp();
  for (int step = 0; step < answer.length; step++) {
    int dropW = answer[step];
    int dropH;
    for (dropH = 0; dropH < H; dropH++) {
      if (temp[dropH][dropW] != 0)
        break;
    }
    if (dropH == H)
      return;

    breakBlock(dropH, dropW);
    down();
  }

  min = Math.min(min, countLeftBlock());
}

```

* breakBlock

애초에 값이 `0`인 곳은 깰 블럭이 없다. 값이 `1`이라면 자신만 깨지기 때문에 `0`으로 값을 수정하고 마친다. `2`이상일경우 퍼져나가는 정도를 저장하여 상하좌우 방향에 대하여 퍼져나가는 길이만큼 `breakBlock`을 호출하여 재귀적으로 처리해준다. 

```java
private static void breakBlock(int dropH, int dropW) {
  if (temp[dropH][dropW] == 0)
    return;
  else if (temp[dropH][dropW] == 1) {
    temp[dropH][dropW] = 0;
    return;
  } else {
    int bound = temp[dropH][dropW] - 1;
    temp[dropH][dropW] = 0;
    for (int i = 1; i <= bound; i++) {
      if (dropH - i < 0)
        break;
      if (temp[dropH - i][dropW] != 0) {
        breakBlock(dropH - i, dropW);
      }
    }

    for (int i = 1; i <= bound; i++) {
      if (dropH + i >= H)
        break;
      if (temp[dropH + i][dropW] != 0) {
        breakBlock(dropH + i, dropW);
      }
    }

    for (int i = 1; i <= bound; i++) {
      if (dropW - i < 0)
        break;
      if (temp[dropH][dropW - i] != 0) {
        breakBlock(dropH, dropW - i);
      }
    }

    for (int i = 1; i <= bound; i++) {
      if (dropW + i >= W)
        break;
      if (temp[dropH][dropW + i] != 0) {
        breakBlock(dropH, dropW + i);
      }
    }
  }

}

```

* down

모든 열의 마지막 행부터 위로 올라가며 탐색한다. 해당 값이 `0`이라면 그보다 윗 행에 `0`아닌 값이 있을때 값을 교환해 주며 블럭을 `down`시켜준다.

```java

private static void down() {
  for (int col = 0; col < W; col++) {
    for (int row = H-1; row >= 0; row--) {
      if (temp[row][col] == 0) {
        boolean flag = true;
        for (int k = row-1; k >= 0; k--) {
          if (temp[k][col] != 0) {
            temp[row][col] = temp[k][col];
            temp[k][col] = 0;
            flag = false;
            break;
          }
        }
        if (flag)
          break;
      }
    }
  }
}

```

* main

```java
StringTokenizer st = new StringTokenizer(br.readLine());
N = Integer.parseInt(st.nextToken());
W = Integer.parseInt(st.nextToken());
H = Integer.parseInt(st.nextToken());

map = new int[H][W];
temp = new int[H][W];
for (int i = 0; i < H; i++) {
  st = new StringTokenizer(br.readLine());
  for (int j = 0; j < W; j++) {
    map[i][j] = Integer.parseInt(st.nextToken());
  }
}

int[] answer = new int[N];
Arrays.fill(answer, -1);
min = H * W;
dfs(0, answer);

if (min == H * W)
  System.out.println("#"+test_case+" 0");
else
  System.out.println("#"+test_case+" "+min);

```

### 3. 의문점

`int[][] dir = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};`를 이용하여 모든 방향에 대하여 처리하는 방식은 SW Expert Academy에서 자주 나오던 방식이였기 때문에, 이번경우도 이 방법을 사용했었다. 하지만, **may be out sync** 라는 에러가 발생하며 이클립스가 멈추는 현상이 일어났다. 남들은 프로젝트 `Refresh`를 해주면 된다던데 나는 해도 안됬다... 결국 각 방향에 대하여 수행하는 네개의 `for`문을 작성하여보았고 정상적으로 작동되는것을 확인하였다.

왜일까..?
