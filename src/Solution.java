import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

class Solution {
	static int N, H, W;
	
	static int min;
	static int[][] map;
	static int[][] temp;
	
	public static void main(String args[]) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());

		for (int test_case = 1; test_case <= T; test_case++) {
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
		}
	}

	private static void dfs(int n, int[] answer) {
		if (n == N) {
			drop(answer);
			return;
		}

		for (int i = 0; i < W; i++) {
			answer[n] = i;
			dfs(n + 1, answer);
		}
	}

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

	private static void initTemp() {
		for (int i = 0; i < H; i++) {
			for (int j = 0; j < W; j++) {
				temp[i][j] = map[i][j];
			}
		}
	}

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

	private static int countLeftBlock() {
		int count = 0;
		for (int i = 0; i < H; i++) 
			for (int j = 0; j < W; j++)
				if (temp[i][j] != 0)
					count++;
		return count;
	}

}