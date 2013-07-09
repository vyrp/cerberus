/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.itabits.cerberus.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import br.itabits.cerberus.MenuActivity;
import br.itabits.cerberus.R;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.UserSettingsFragment;

public class FacebookLoginFragmentActivity extends FragmentActivity{
	private UserSettingsFragment userSettingsFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_fragment_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
      
        userSettingsFragment = (UserSettingsFragment) fragmentManager.findFragmentById(R.id.login_fragment);
        userSettingsFragment.clearPermissions();
        userSettingsFragment.setSessionStatusCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                Log.d("LoginUsingLoginFragmentActivity", String.format("New session state: %s", state.toString()));
                if(session.isOpened()){
                	 // make request to the /me API
                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
                      // callback after Graph API response with user object
                      @Override
                      public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                        	// deliver the result to close LoginActivity
            				Intent returnIntent = new Intent();
            				returnIntent.putExtra(LoginActivity.EXTRA_FACEBOOK, LoginActivity.FACEBOOK_SUCCESS);
            				setResult(RESULT_OK, returnIntent);

                        	// Uses the name of the facebook to the next activity
            				Intent openBorrow = new Intent(FacebookLoginFragmentActivity.this,	MenuActivity.class);
            				openBorrow.putExtra(LoginActivity.EXTRA_EMAIL, user.getName());
            				startActivity(openBorrow);
            				
            				// closes both login and facebook login
            				finish();
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

}
