import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

public class Main {
	static int l, n, q;
	static int[][] map, position;
	static Knight[] klist;
	static int[] dr = { -1, 0, 1, 0 }; // 0 : 위쪽, 1: 오른쪽, 2: 아래쪽, 3: 왼쪽
	static int[] dc = { 0, 1, 0, -1 };
	static ArrayList<Integer> moving;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		l = Integer.parseInt(st.nextToken());
		n = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());

		map = new int[l + 1][l + 1];
		for (int i = 1; i <= l; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 1; j <= l; j++) {
				map[i][j] = Integer.parseInt(st.nextToken()); // 0:빈칸, 1:함정, 2:벽
			}
		}

		klist = new Knight[n + 1];
		for (int i = 1; i <= n; i++) {
			st = new StringTokenizer(br.readLine());
			int tr = Integer.parseInt(st.nextToken());
			int tc = Integer.parseInt(st.nextToken());
			int th = Integer.parseInt(st.nextToken());
			int tw = Integer.parseInt(st.nextToken());
			int tk = Integer.parseInt(st.nextToken());

			klist[i] = new Knight(tr, tc, th, tw, tk);
		}

		while (q-- > 0) {
			// 기사 이동
			st = new StringTokenizer(br.readLine());
			int ordered = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());

			// 체스판에서 사라진 기사에게 명령을 내리면 아무런 반응이 없게 됩니다.
			if (klist[ordered].k <= 0)
				continue;

			// 장벽 표시
			position = new int[l + 1][l + 1];
			for (int i = 1; i <= n; i++) {
				if (klist[i].k <= 0) // 탈락한 기사
					continue;

				for (int ti = 0; ti < klist[i].h; ti++) {
					for (int tj = 0; tj < klist[i].w; tj++) {
						position[klist[i].r + ti][klist[i].c + tj] = i;
					}
				}
			}
			
			moving = new ArrayList<Integer>();
			dfs(ordered, d);
			if (moving.isEmpty())
				continue;
			
			for (int now : moving) {
				klist[now].r += dr[d];
				klist[now].c += dc[d];
				
				if (now == ordered)
					continue;
				
				int mr = klist[now].r;
				int mc = klist[now].c;
				int cnt = 0;
				for (int i = 0; i < klist[now].h; i++) {
					for (int j = 0; j < klist[now].w; j++) {
						if (map[mr + i][mc + j] == 1) {
							cnt++;
						}
					}
				}
				
				klist[now].damage += cnt;
				klist[now].k -= cnt;
			}
		}

		int answer = 0;
		for (int i = 1; i <= n; i++) {
			if (klist[i].k > 0) {
				answer += klist[i].damage;
			}
		}
		System.out.println(answer);
	}

	static boolean dfs(int now, int dir) {
		moving.add(now);
		
		int tr = klist[now].r;
		int tc = klist[now].c;
		if (dir == 0) {
			tr -= 1;

			for (int gap = 0; gap < klist[now].w; gap++) {
				if (tc + gap <= 0 || tc + gap > l) {
					moving.clear();
					return false;
				}
				if (map[tr][tc + gap] == 2) {
					moving.clear();
					return false;
				}

				if (position[tr][tc + gap] != 0) {
					if (!dfs(position[tr][tc + gap], dir)) {
						return false;
					}
				}
			}
		} else if (dir == 1) {
			tc += klist[now].w;

			for (int gap = 0; gap < klist[now].h; gap++) {
				if (tr + gap <= 0 || tr + gap > l){
					moving.clear();
					return false;
				}
				if (map[tr + gap][tc] == 2) {
					moving.clear();
					return false;
				}

				if (position[tr + gap][tc] != 0) {
					if (!dfs(position[tr + gap][tc], dir))
						return false;
				}
			}
		} else if (dir == 2) {
			tr += klist[now].h;

			for (int gap = 0; gap < klist[now].w; gap++) {
				if (tc + gap <= 0 || tc + gap > l) {
					moving.clear();
					return false;
				}

				if (map[tr][tc + gap] == 2){
					moving.clear();
					return false;
				}
				
				if (position[tr][tc + gap] != 0) {
					if (!dfs(position[tr][tc + gap], dir))
						return false;
				}
			}
		} else if (dir == 3) {
			tc -= 1;

			for (int gap = 0; gap < klist[now].h; gap++) {
				if (tr + gap <= 0 || tr + gap > l) {
					moving.clear();
					return false;
				}
				if (map[tr + gap][tc] == 2) {
					moving.clear();
					return false;
				}
				
				if (position[tr + gap][tc] != 0) {
					if (!dfs(position[tr + gap][tc], dir))
						return false;
				}
			}
		}
		
		return true;
	}

	static class Knight {
		public int r;
		public int c;
		public int h;
		public int w;
		public int k;
		public int damage;

		public Knight(int r, int c, int h, int w, int k) {
			// TODO Auto-generated constructor stub
			this.r = r;
			this.c = c;
			this.h = h;
			this.w = w;
			this.k = k;
			this.damage = 0;
		}
	}

}