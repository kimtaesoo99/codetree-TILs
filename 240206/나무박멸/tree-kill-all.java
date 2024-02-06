import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

public class Main {

    private static int[][] map;
    private static int[][] ban;
    private static int n;
    private static Deque<int[]> location;
    private static Deque<Integer> value;
    private static int answer;

    private static final int[] dy = {1, 0, -1, 0};
    private static final int[] dx = {0, 1, 0, -1};

    private static final int[] by = {1, 1, -1, -1};
    private static final int[] bx = {1, -1, 1, -1};

    public static void main(String args[]) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken()); // 격자 크기
        int m = Integer.parseInt(st.nextToken()); // 박멸 진행 년 수
        int k = Integer.parseInt(st.nextToken()); // 확산 범위
        int c = Integer.parseInt(st.nextToken()); // 제초제 남은 기간

        map = new int[n][n];
        ban = new int[n][n];
        location = new ArrayDeque<>();
        value = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < m; i++) {
            grow();
            breeding();
            findBan(k, c + 1);
            decreaseBan();
        }

        System.out.println(answer);
    }

    private static void grow() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] > 0) {
                    for (int k = 0; k < 4; k++) {
                        int moveY = i + dy[k];
                        int moveX = j + dx[k];

                        if (isCorrectIndex(moveY, moveX) && map[moveY][moveX] > 0 && ban[moveY][moveX] == 0) {
                            map[i][j]++;
                        }
                    }
                }
            }
        }
    }

    private static void breeding() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int count = 0;
                if (map[i][j] > 0) {
                    for (int k = 0; k < 4; k++) {
                        int moveY = i + dy[k];
                        int moveX = j + dx[k];

                        if (isCorrectIndex(moveY, moveX) && map[moveY][moveX] == 0 && ban[moveY][moveX] == 0) {
                            location.offer(new int[]{moveY, moveX});
                            count++;
                        }
                    }

                    for (int k = 0; k < count; k++) {
                        value.offer(map[i][j] / count);
                    }
                }
            }
        }

        while (!location.isEmpty()) {
            int[] now = location.pollLast();
            map[now[0]][now[1]] += value.pollLast();
        }
    }

    private static void findBan(int k,int c) {
        int max = 0;
        int resultY = 0;
        int resultX = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (ban[i][j] == 0 && map[i][j] >= 0) {
                    int sum = map[i][j];
                    for (int l = 0; l < 4; l++) {
                        int moveY = i + by[l];
                        int moveX = j + bx[l];

                        for (int m = 0; m < k; m++) {
                            if (!isCorrectIndex(moveY, moveX) || ban[moveY][moveX] > 0 || map[moveY][moveX] == -1) {
                                break;
                            }
                            sum += map[moveY][moveX];
                            moveY += by[l];
                            moveX += bx[l];
                        }
                    }
                    if (max < sum) {
                        max = sum;
                        resultY = i;
                        resultX = j;
                    }
                }
            }
        }

        map[resultY][resultX] = 0;
        ban[resultY][resultX] = c;

        for (int l = 0; l < 4; l++) {
            int moveY = resultY + by[l];
            int moveX = resultX + bx[l];

            for (int m = 0; m < k; m++) {
                if (!isCorrectIndex(moveY, moveX) || ban[moveY][moveX] > 1 || map[moveY][moveX] == -1) {
                    break;
                }
                map[moveY][moveX] = 0;
                ban[moveY][moveX] = c;
                moveY += by[l];
                moveX += bx[l];
            }
        }

        answer += max;
    }

    private static void decreaseBan() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (ban[i][j] > 0) {
                    ban[i][j]--;
                }
            }
        }
    }

    private static boolean isCorrectIndex(int y, int x) {
        return 0 <= y && y < n && 0 <= x && x < n;
    }
}