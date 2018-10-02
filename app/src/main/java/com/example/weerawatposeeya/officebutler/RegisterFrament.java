package com.example.weerawatposeeya.officebutler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class RegisterFrament extends Fragment{
//    Explicit

    private Boolean aBoolean = true;
    private Uri uri ;
    private ImageView imageView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        create tool bar

        createToolBar();

        avatarController();

    }// Main method

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == getActivity().RESULT_OK) {
            aBoolean = false;
            uri = data.getData();

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                //Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap,)
                //imageView.strng
            }catch (Exception e){
                Log.d("20ctV1", "e  ===> " + e.toString());
            }
        } else {
            MyAlert myAlert = new MyAlert(getActivity());
            myAlert.normalDialog(getString(R.string.title_avatar), getString(R.string.message_avatar));
        }
    }

    private void avatarController() {
        imageView = getView().findViewById(R.id.imageavatar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Please choose app"), 1);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itemupdate){

            checkAvatarAndContent();
            return true;
        }



        return super.onOptionsItemSelected(item);

    }

    private void checkAvatarAndContent() {

        MyAlert myAlert = new MyAlert(getActivity());
        EditText nameEditText = getView().findViewById(R.id.editemail);
        EditText passwordEditText = getView().findViewById(R.id.editpassword);

        String nameString = nameEditText.getText().toString().trim();
        String passwordString = passwordEditText.getText().toString().trim();

        if (aBoolean){
//            Not choose avatar
            myAlert.normalDialog(getString(R.string.title_space), getString(R.string.title_space));

        } else if (nameString.isEmpty() || passwordString.isEmpty()) {
            myAlert.normalDialog(getString(R.string.title_space), getString(R.string.message_space));

        } else {
            
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_register, menu);
    }

    private void createToolBar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbarRegister);

        ((MainActivity)getActivity()).setSupportActionBar(toolbar);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.regis));

        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen_register, container, false);
        return view;
    }


}
