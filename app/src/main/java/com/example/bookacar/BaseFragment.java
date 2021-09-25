package com.example.bookacar;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    private final CustomProgressDialogFragment customProgressDialogFragment = new CustomProgressDialogFragment();

    public void showProgressDialog(boolean show) {
        try {
            if (show) {
                if (!customProgressDialogFragment.isShowing()) {
                    customProgressDialogFragment.setShowing(true);
                    customProgressDialogFragment.show(getActivity().getSupportFragmentManager(), "");
                }
            } else {
                if (customProgressDialogFragment.isShowing()) {
                    customProgressDialogFragment.dismiss();
                    customProgressDialogFragment.setShowing(false);
                }
            }
        } catch (Exception ex) {

        }
    }
}
