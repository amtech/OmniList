package me.shouheng.omnilist.fragment.setting;

import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;

import com.afollestad.materialdialogs.MaterialDialog;

import me.shouheng.omnilist.PalmApp;
import me.shouheng.omnilist.R;
import me.shouheng.omnilist.listener.OnFragmentDestroyListener;
import me.shouheng.omnilist.utils.ToastUtils;
import me.shouheng.omnilist.utils.preferences.UserPreferences;

public class SettingsPreferences extends BaseFragment {

    private UserPreferences userPreferences;

    private Preference prefVideo;

    public static SettingsPreferences newInstance() {
        Bundle args = new Bundle();
        SettingsPreferences fragment = new SettingsPreferences();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userPreferences = UserPreferences.getInstance();

        configToolbar();

        addPreferencesFromResource(R.xml.preferences_preferences);

        prefVideo = findPreference(R.string.key_video_size_limit);
        prefVideo.setOnPreferenceClickListener(preference -> {
            showVideoLimitEditor();
            return true;
        });
        updateVideoSizePref();
    }

    private void configToolbar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.setting_preferences);
        }
    }

    private void showVideoLimitEditor() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.video_limit)
                .content(R.string.setting_video_limit_size)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .inputRange(0, 2)
                .negativeText(R.string.text_cancel)
                .input(getString(R.string.video_limit_input_hint), String.valueOf(userPreferences.getVideoSizeLimit()),
                        (materialDialog, charSequence) -> {
                            try {
                                int size = Integer.parseInt(charSequence.toString());
                                if (size < 0) {
                                    ToastUtils.makeToast(R.string.illegal_number);
                                } else {
                                    userPreferences.setVideoSizeLimit(size);
                                    updateVideoSizePref();
                                }
                            } catch (Exception e) {
                                ToastUtils.makeToast(R.string.wrong_numeric_string);
                            }
                        }).show();
    }

    private void updateVideoSizePref() {
        prefVideo.setSummary(String.format(
                PalmApp.getStringCompact(R.string.setting_video_limit_size_sub),
                userPreferences.getVideoSizeLimit()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof OnFragmentDestroyListener) {
            ((OnFragmentDestroyListener) getActivity()).onFragmentDestroy();
        }
    }
}
