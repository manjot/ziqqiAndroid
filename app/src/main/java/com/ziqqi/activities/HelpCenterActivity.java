package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ziqqi.R;
import com.ziqqi.databinding.ActivityHelpCenterBinding;
import com.ziqqi.databinding.ActivityMyAddressBookBinding;
import com.ziqqi.model.helpcentermodel.HelpCenterModel;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.viewmodel.HelpCenterViewModel;
import com.ziqqi.viewmodel.MyAddressViewModel;

public class HelpCenterActivity extends AppCompatActivity {

    HelpCenterViewModel helpCenterViewModel;
    ActivityHelpCenterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help_center);
        helpCenterViewModel = ViewModelProviders.of(this).get(HelpCenterViewModel.class);
        binding.executePendingBindings();
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        fetchHelps();

    }

    private void fetchHelps() {
        binding.progressBar.setVisibility(View.VISIBLE);
        helpCenterViewModel.fetchHelpCenter();
        helpCenterViewModel.getHelpResponse().observe(this, new Observer<HelpCenterModel>() {
            @Override
            public void onChanged(@Nullable final HelpCenterModel helpCenterModel) {
                if (!helpCenterModel.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.tvHelpFirst.setText(helpCenterModel.getPayload().get(0).getTitle());
                    binding.tvHelpSecond.setText(helpCenterModel.getPayload().get(1).getTitle());
                    binding.tvHelpThird.setText(helpCenterModel.getPayload().get(2).getTitle());
                    binding.tvHelpFourth.setText(helpCenterModel.getPayload().get(3).getTitle());
                    binding.tvHelpFifth.setText(helpCenterModel.getPayload().get(4).getTitle());
                    binding.tvHelpSixth.setText(helpCenterModel.getPayload().get(5).getTitle());

                    binding.llOne.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), helpCenterModel.getPayload().get(0).getId(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
