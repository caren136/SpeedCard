/**
 * Copyright(C) 2016 crane カードプロジェクト
 * Licensed under the Apache License Version 2.0 (the "License");
 * you may not use this file export in compliance with the License.
 * you may obtaion a copy of the License at
 *
 *      http://www.apache.org/license/LICENSE-2.0
 *
* Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License fro the specific language governing permissions and
 * limitations under the License.
 */
package jp.ac.shohoku.crane.speedcard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author s15e108 一郷侑香 s15e136 下川琴鈴
 */
public class SpeedCardView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    public static final int LV1_DISP=1;  // レベル1スタート表示
    public static final int LV1_PLAY=2;  // レベル1プレイ中
    public static final int LV2_DISP=3;  // レベル2スタート表示
    public static final int LV2_PLAY=4;  // レベル2プレイ中
    public static final int LV3_DISP=5;  // レベル3スタート表示
    public static final int LV3_PLAY=6;  // レベル3プレイ中
    public static final int LV4_DISP=7;  // レベル4スタート表示
    public static final int LV4_PLAY=8;  // レベル4プレイ中
    public static final int GAME_OVER=100;  // ゲームオ－バー
    public static final int GAME_CLEAR=101;  // ゲームクリア

    public static int NEUX7_WIDTH=800;
    public static int NEUX7_HEIGHT=1280;

    private SurfaceHolder mHolder;
    private int mGameStart;  // ゲームの状態を表す変数

    /**
     * コンストラクタ<br />
     * 引数はContextとAttributeSet
     *
     * @param context
     * @param attrs
     */
    public SpeedCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初期歌謡のメソッド<br />
     * 各種変数の初期化やコールバックの割り当てなどを行う
     */
    private void init() {
        mHolder = getHolder();  // SurfaceHolderを取得する。
        mHolder.addCallback(this);
        setFocusable(true);  // フォーカスを当てることを可能にするメソッド
        requestFocus();  // フォーカスを要求して実行を可能にする
        mGameStart = LV1_DISP;  // 最初はレベル1表示画面
    }

    /**
     * 定期的に実行するスレッドを生成し、定期的に実行の設定を行う<br />
     * このメソッドはサーフェスが生成されたタイミングで実行される。
     */
    private void start() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        // scheduleAtFixedRateの第1引数：実行可能なクラス。第4引数：ミリ病に設定している
        // 第2引数は実行を開始する時刻、第3引数は実行する間隔：
        executor.scheduleAtFixedRate(this, 30, 30, TimeUnit.MILLISECONDS);
    }

    /*
     * @see
     * android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    /*
     * サーフェスが生成されたとき、とりあえず画面に表示し、その後定期的に実行するスレッドをスタート
     *
     * @see
     *  android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
     */
    public void surfaceCreated(SurfaceHolder holder) {
        draw();
        start();
    }

    /*
     * @see  android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
     */
    public void surfaceDestroyed(SurfaceHolder holder) { }

    /**
     * イベント処理するためのメソッド
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {  // イベントの種類によって処理を振り分ける
            case MotionEvent.ACTION_DOWN:  // 画面上で押下されたとき
                switch (mGameStart) {  // ゲームの状態によって処理を振り分ける
                    case LV1_DISP:
                        mGameStart = LV1_PLAY;
                        break;
                    case LV1_PLAY:
                        mGameStart = LV2_DISP;
                        break;
                    case LV2_DISP:
                        mGameStart = LV2_PLAY;
                        break;
                    case LV2_PLAY:
                        mGameStart = LV3_DISP;
                        break;
                    case LV3_DISP:
                        mGameStart = LV3_PLAY;
                        break;
                    case LV3_PLAY:
                        mGameStart = LV4_DISP;
                        break;
                    case LV4_DISP:
                        mGameStart = LV4_PLAY;
                        break;
                    case LV4_PLAY:
                        mGameStart = GAME_OVER;
                        break;
                    case GAME_OVER:
                        mGameStart = GAME_CLEAR;
                        break;
                    case GAME_CLEAR:
                        mGameStart = LV1_DISP;
                        break;
                }
                break;
        }
        return true;
    }

    /**
     * 描画用メソッド<br />
     * 画面への描画処理はすべてこの中に書く
     */
    private void draw() {
        Canvas canvas = mHolder.lockCanvas(); // サーフェースをロックする
        canvas.drawColor(Color.WHITE);  // キャンバスを白に塗る

        String msg;

        Paint paint = new Paint();
        paint.setTextSize(30);
        switch (mGameStart) {  // ゲームの状態によって処理を振り分ける
            case LV1_DISP:
                // LV1_DISPのときの描画処理
                paint.setTextSize(80);
                canvas.drawColor(Color.rgb(230, 207, 204));
                msg = "LEVEL1 DISP";
                canvas.drawText(msg, 350, 400, paint);
                break;
            case LV1_PLAY:
                // LV1_PLAYのときの描画処理
                msg = "LEVEL1 PLAY";
                canvas.drawText(msg, 10, 50, paint);
                break;
            case LV2_DISP:
                // LV2_DISPのときの描画処理
                paint.setTextSize(80);
                canvas.drawColor(Color.rgb(227, 230, 204));
                msg = "LEVEL2 DISP";
                canvas.drawText(msg, 350, 400, paint);
                break;
            case LV2_PLAY:
                // LV2_PLAYのときの描画処理
                msg = "LEVEL2 PLAY";
                canvas.drawText(msg, 10, 50, paint);
                break;
            case LV3_DISP:
                // LV3_DISPのときの描画処理
                paint.setTextSize(80);
                canvas.drawColor(Color.rgb(204, 230, 207));
                msg = "LEVEL3 DISP";
                canvas.drawText(msg, 350, 400, paint);
                break;
            case LV3_PLAY:
                // LV3_PLAYのときの描画処理
                msg = "LEVEL3 PLAY";
                canvas.drawText(msg, 10, 50, paint);
                break;
            case LV4_DISP:
                // LV4_DISPのときの描画処理
                paint.setTextSize(80);
                canvas.drawColor(Color.rgb(204, 227, 230));
                msg = "LEVEL4 DISP";
                canvas.drawText(msg, 350, 400, paint);
                break;
            case LV4_PLAY:
                // LV4_PLAYのときの描画処理
                msg = "LEVEL4 PLAY";
                canvas.drawText(msg, 10, 50, paint);
                break;
            case GAME_OVER:
                // ゲームオーバーのときの描画処理
                paint.setTextSize(80);
                canvas.drawColor(Color.rgb(207, 204, 230));
                msg = "GAME OVER";
                canvas.drawText(msg, 350, 400, paint);
                break;
            case GAME_CLEAR:
                // ゲームクリアのときの描画処理
                paint.setTextSize(80);
                canvas.drawColor(Color.rgb(230, 205, 227));
                msg = "GAME CLEAR";
                canvas.drawText(msg, 350, 400, paint);
                break;
        }
        mHolder.unlockCanvasAndPost(canvas);  // サーフェースのロックを外す
    }

    /*
     * 実行可能メソッド。このクラスの中では定期実行される
     *
     * @see java.lang.Runable#run()
     */
    public void run() {
        draw();
    }
}
