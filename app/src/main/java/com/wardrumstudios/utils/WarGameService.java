package com.wardrumstudios.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements;
import com.google.android.gms.games.leaderboard.LeaderboardBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.games.snapshot.Snapshots;
import java.util.ArrayList;
import java.util.List;

public class WarGameService implements /*GameHelper.GameHelperListener, ResultCallback<AppStateManager.StateResult>,*/ WarActivityLifecycleListener {
    public static final int CLIENT_ALL = 15;
    public static final int CLIENT_APPSTATE = 4;
    public static final int CLIENT_DRIVE = 8;
    public static final int CLIENT_GAMES = 1;
    public static final int CLIENT_PLUS = 2;
    public static final int RC_SHOWSNAPSHOTLIST = 8321;
    private static final String TAG = "WarGameService";
    protected boolean creatingGamehelper = true;
    private boolean isPaused = false;
    /* access modifiers changed from: private */
    public WarBase mActivity;
    protected boolean mDebugLog = true;
    protected String mDebugTag = TAG;
    //protected GameHelper mHelper;
    protected int mRequestedClients = 1;
    byte[] savedScreenshot = null;

    public native void notifyAchievementsLoaded(String[] strArr, boolean[] zArr, int[] iArr, int[] iArr2);

    public native void notifyLeaderboardScoreQuery(int i, float[] fArr, String[] strArr);

    public native void notifySignInChange(boolean z);

    public native void notifySignInFailed();

    public native void notifySnapshotCountLoaded(int i);

    public native void notifySnapshotLoading();

    public native void notifySnapshotSelected(byte[] bArr);

    public native void notifyStateConflict(int i, String str, byte[] bArr, byte[] bArr2);

    public native void notifyStateLoaded(int i, int i2, byte[] bArr);

    protected WarGameService(WarBase activity) {
        this.mActivity = activity;
        this.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                /*this.mActivity.AddLifecycleListener(this);
                this.mHelper = WarGameService.this.getGameHelper();
                this.setRequestedClients(15);
                this.mHelper.setup(this);
                this.onStart();
                this.creatingGamehelper = false;*/
            }
        });
        int hangCount = 0;
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            while (this.creatingGamehelper) {
                int hangCount2 = hangCount + 1;
                if (hangCount < 10) {
                    this.mActivity.mSleep(100);
                    hangCount = hangCount2;
                } else {
                    return;
                }
            }
        }
    }

    /*public GameHelper getGameHelper() {
        if (this.mHelper == null) {
            this.mHelper = new GameHelper(this.mActivity, this.mRequestedClients);
            this.mHelper.enableDebugLog(this.mDebugLog);
        }
        return this.mHelper;
    }*/

    /* access modifiers changed from: protected */
    public void setRequestedClients(int requestedClients) {
        this.mRequestedClients = requestedClients;
    }

    public void onActivityResult(int request, int response, Intent data) {
        if (request == 8321) {
            switch (response) {
                case -1:

                    return;
                default:
                    return;
            }
        } else {
            //this.mHelper.onActivityResult(request, response, data);
        }
    }

    /* access modifiers changed from: protected */
    /*public GoogleApiClient getApiClient() {
        return this.mHelper.getApiClient();
    }*/

    /* access modifiers changed from: protected */
    /*public boolean isSignedIn() {
        return this.mHelper.isSignedIn();
    }*/

    public void onStart() {
        //this.mHelper.onStart(this.mActivity);
    }

    public void onPause() {
        this.isPaused = true;
        debugLog("onPause OpenSnapshotResult mActivity.paused " + this.mActivity.paused);
        //this.mHelper.onPause();
    }

    public void onStop() {
        //this.mHelper.onStop();
    }

    public void onDestroy() {
        //this.mHelper.onStop();
    }

    public void onResume() {
        this.isPaused = false;
    }

    public void onSignInSucceeded() {
        /*Games.Achievements.load(getApiClient(), false).setResultCallback(new ResultCallback<Achievements.LoadAchievementsResult>() {
            public void onResult(Achievements.LoadAchievementsResult result) {
                int statusCode = result.getStatus().getStatusCode();
                AchievementBuffer buffer = result.getAchievements();
                WarGameService.this.debugLog("Achivements loaded!");
                if (statusCode != 0) {
                    WarGameService.this.debugLog("Error while loading achievements.");
                    return;
                }
                int count = buffer.getCount();
                String[] ids = new String[count];
                boolean[] states = new boolean[count];
                int[] steps = new int[count];
                int[] totalSteps = new int[count];
                for (int i = 0; i < count; i++) {
                    ids[i] = buffer.get(i).getAchievementId();
                    states[i] = buffer.get(i).getState() == 0;
                    if (buffer.get(i).getType() == 1) {
                        steps[i] = buffer.get(i).getCurrentSteps();
                        totalSteps[i] = buffer.get(i).getTotalSteps();
                    } else {
                        steps[i] = -1;
                        totalSteps[i] = -1;
                    }
                }
                WarGameService.this.debugLog("Notifying down to native");
                WarGameService.this.notifyAchievementsLoaded(ids, states, steps, totalSteps);
                WarGameService.this.debugLog("Done. Notifying down to native");
                buffer.close();
            }
        });
        notifySignInChange(true);*/
    }

    public void onSignInFailed() {
        notifySignInFailed();
    }

    public void onLeaderboardMetadataLoaded(int statusCode, LeaderboardBuffer buffer) {
    }

    /* access modifiers changed from: protected */
    public void enableDebugLog(boolean enabled, String tag) {
        this.mDebugLog = true;
        this.mDebugTag = tag;
        /*if (this.mHelper != null) {
            this.mHelper.enableDebugLog(enabled);
        }*/
    }

    /* access modifiers changed from: package-private */
    public void debugLog(String message) {
        if (this.mDebugLog) {
            Log.d(this.mDebugTag, "WarGameService: " + message);
        }
    }

    /* access modifiers changed from: protected */
    /*public String getInvitationId() {
        return this.mHelper.getInvitationId();
    }*/

    /* access modifiers changed from: protected */
    /*9public boolean hasSignInError() {
        return this.mHelper.hasSignInError();
    }*/

    /* access modifiers changed from: protected */
    /*public GameHelper.SignInFailureReason getSignInError() {
        return this.mHelper.getSignInError();
    }

    public void ShowSignInUI() {
        if (!this.mHelper.isSignedIn()) {
            this.mHelper.beginUserInitiatedSignIn();
        }
    }

    public void SignOut() {
        this.mHelper.signOut();
    }

    public boolean GetConnectionStatus() {
        return this.mHelper.isSignedIn();
    }

    public void RefreshData(boolean forceReload) {
    }

    public String GetPlayerName() {
        try {
            if (!this.mHelper.isSignedIn()) {
                return "default";
            }
            String playerName = Games.Players.getCurrentPlayer(getApiClient()).getDisplayName();
            System.out.println("GetPlayerName " + playerName);
            return playerName;
        } catch (Exception e) {
            return "default";
        }
    }

    public void ShowAchievementList() {
        if (this.mHelper.isSignedIn()) {
            this.mActivity.startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), 0);
        } else {
            debugLog("The GamesClient is not connected so the achievement list cannot be shown.");
        }
    }

    public void UnlockAchievement(String id) {
        debugLog("Unlocking Achievement: " + id);
        if (this.mHelper.isSignedIn()) {
            Games.Achievements.unlock(getApiClient(), id);
            debugLog("Done Unlocking Achievement: " + id);
            return;
        }
        debugLog("Trying to unlock an achievement while the achievements are not available.");
    }

    public void IncrementAchievement(String id, int numSteps) {
        if (this.mHelper.isSignedIn()) {
            Games.Achievements.increment(getApiClient(), id, numSteps);
        } else {
            debugLog("Trying to unlock an achievement but we're not signed in.");
        }
    }

    public void ShowLeaderboards() {
        if (this.mHelper.isSignedIn()) {
            this.mActivity.startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()), 0);
        } else {
            debugLog("The GamesClient is not connected so the leaderboards cannot be shown.");
        }
    }

    public void ShowLeaderboard(String id) {
        if (this.mHelper.isSignedIn()) {
            this.mActivity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), this.mActivity.GetLeaderboardId(id)), 0);
            return;
        }
        debugLog("The GamesClient is not connected so the requested leaderboard cannot be shown.");
    }

    public void SubmitScore(String leaderboardId, long score) {
        if (this.mHelper.isSignedIn()) {
            Games.Leaderboards.submitScore(getApiClient(), this.mActivity.GetLeaderboardId(leaderboardId), score);
            return;
        }
        debugLog("The GamesClient is not connected so the new score cannot be submitted to the leaderboards cannot be shown.");
    }

    class LeaderboardQuery {
        int queryId;
        PendingResult<Leaderboards.LoadScoresResult> result;

        LeaderboardQuery() {
        }
    }

    public void SubmitLeaderboardQuery(String leaderboardName, int numResuts, int startIndex, int queryId, boolean showFriends, boolean sortAscending) {
        if (this.mHelper.isSignedIn()) {
            String gameLeaderboardId = this.mActivity.GetLeaderboardId(leaderboardName);
            debugLog("SubmitLeaderboardQuery " + gameLeaderboardId);
            int friendCollection = showFriends ? 1 : 0;
            System.out.println("SubmitLeaderboardQuery " + gameLeaderboardId + " friendCollection " + friendCollection);
            processPendingResult(queryId, Games.Leaderboards.loadTopScores(getApiClient(), gameLeaderboardId, 2, friendCollection, numResuts, true));
            return;
        }
        debugLog("The GamesClient is not connected so the requested leaderboard cannot be shown.");
    }

    public void CancelLeaderboardQuery(int queryId) {
        debugLog("CancelLeaderboardQuery");
    }

    public void getPlayerCenteredScores(String leaderboardID, int timeSpan, int collection) {
        PendingResult<Leaderboards.LoadScoresResult> loadPlayerCenteredScores = Games.Leaderboards.loadPlayerCenteredScores(getApiClient(), leaderboardID, timeSpan, collection, 25, true);
    }*/

    /* access modifiers changed from: protected */
    public void processPendingResult(int queryId, PendingResult<Leaderboards.LoadScoresResult> pendingScores) {
        final int qQueryId = queryId;
        pendingScores.setResultCallback(new ResultCallback<Leaderboards.LoadScoresResult>() {
            public void onResult(Leaderboards.LoadScoresResult loadScoresResult) {
                WarGameService.this.notifyScoresLoaded(qQueryId, WarGameService.this.extractScores(loadScoresResult));
            }
        });
    }

    /* access modifiers changed from: protected */
    public List<LeaderboardScore> extractScores(Leaderboards.LoadScoresResult loadScoresResult) {
        LeaderboardScoreBuffer buffer = loadScoresResult.getScores();
        List<LeaderboardScore> leaderboardScores = new ArrayList<>();
        for (int i = 0; i < buffer.getCount(); i++) {
            leaderboardScores.add(buffer.get(i));
        }
        return leaderboardScores;
    }

    public void notifyScoresLoaded(int queryId, List<LeaderboardScore> scoreList) {
        System.out.println("notifyScoresLoaded " + scoreList.size());
        float[] scores = new float[scoreList.size()];
        String[] names = new String[scoreList.size()];
        for (int i = 0; i < scoreList.size(); i++) {
            scores[i] = (float) scoreList.get(i).getRawScore();
            names[i] = scoreList.get(i).getScoreHolder().getDisplayName();
            System.out.println("notifyScoresLoaded " + names[i] + " score " + scores[i] + " scorestr " + scoreList.get(i).getDisplayScore());
        }
        notifyLeaderboardScoreQuery(queryId, scores, names);
    }

    public void SaveToCloud(byte[] data) {
        /*if (this.mHelper.isSignedIn()) {
            AppStateManager.updateImmediate(getApiClient(), 0, data).setResultCallback(this);
        } else {
            debugLog("The GamesClient is not connected so data cannot be saved to the cloud.");
        }*/
    }

    public void LoadFromCloud() {
        /*if (this.mHelper.isSignedIn()) {
            AppStateManager.load(getApiClient(), 0).setResultCallback(this);
        } else {
            debugLog("The GamesClient is not connected so data cannot be saved to the cloud.");
        }*/
    }

    public void ResolveState(String resolvedVersion, byte[] data) {
       // AppStateManager.resolve(getApiClient(), 0, resolvedVersion, data).setResultCallback(this);
    }


    public void DeleteAllSnapshots() {
        throw new UnsupportedOperationException();
    }
}
