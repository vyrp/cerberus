package br.itabits.cerberus.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import br.itabits.cerberus.R;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.UserSettingsFragment;

/**
 * As an alternative of log in, the user can choose to validate
 * his ID by logging in with a facebook account.<br>
 * This class contain references to facebook SDK and create fragments
 * that interact with the facebook API to generate a valid authentication.
 * <br><br>
 * (this class was based on a facebook SDK sample)
 * @author Marcelo
 *
 */
public class FacebookLoginFragmentActivity extends FragmentActivity {
	private UserSettingsFragment userSettingsFragment;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_fragment_activity);
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		userSettingsFragment = (UserSettingsFragment) fragmentManager.findFragmentById(R.id.login_fragment);
		userSettingsFragment.setSessionStatusCallback(new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) {

				if (session.isOpened()) {
					// make request to the /me API
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						// callback after Graph API response with user object
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if (user != null) {
								// deliver the result to close LoginActivity
								Intent returnIntent = new Intent();
								returnIntent.putExtra(LoginActivity.EXTRA_FACEBOOK, LoginActivity.FACEBOOK_SUCCESS);
								returnIntent.putExtra(LoginActivity.EXTRA_USER_ID,  user.getName());
								setResult(RESULT_OK, returnIntent);

							}
						}
					});
				}

			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		userSettingsFragment.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);

	}
	
	/**
	 * In each log in session, the user must be request user and password, 
	 * so after the session, the user is disconnected.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		Session mSession = Session.getActiveSession();
		if (mSession != null) {
			mSession.closeAndClearTokenInformation();
		}
	}
}
