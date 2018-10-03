package com.example.weerawatposeeya.officebutler;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
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
import android.widget.Toast;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

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

    public class uploadListener implements FTPDataTransferListener{

        @Override
        public void started() {
            Toast.makeText(getActivity(), "Start upload", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void transferred(int i) {
            Toast.makeText(getActivity(), "continous", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void completed() {
            Toast.makeText(getActivity(), "complete", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void aborted() {

        }//undo by user

        @Override
        public void failed() {

        }//cancel by acident
    }//upload class



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == getActivity().RESULT_OK) {
            aBoolean = false;
            uri = data.getData();

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 800, 600, false);
                imageView.setImageBitmap(bitmap1);
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
        EditText nameEditText = getView().findViewById(R.id.editname);
        EditText passwordEditText = getView().findViewById(R.id.editpassword);
        EditText emailEditText = getView().findViewById(R.id.editemail);

        String nameString = nameEditText.getText().toString().trim();
        String passwordString = passwordEditText.getText().toString().trim();
        String emaiString = emailEditText.getText().toString().trim();

        if (aBoolean){
//            Not choose avatar
            myAlert.normalDialog(getString(R.string.title_space), getString(R.string.title_space));

        } else if (nameString.isEmpty() || passwordString.isEmpty()) {
            myAlert.normalDialog(getString(R.string.title_space), getString(R.string.message_space));

        } else {
//            find PAth_of Choose Image
            String pathImageString = null;
            String[] strings = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(uri, strings, null,null,null);
            if (cursor != null) {
                cursor.moveToFirst();//บนลงล่าง
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                pathImageString = cursor.getString(index);
            } else {
                pathImageString = uri.getPath();
            }
            Log.d("20ctV1", "path ==> " + pathImageString);//get path string§

//            Find Name of choose image
            String nameImageString = pathImageString.substring(pathImageString.lastIndexOf("/"));
            Log.d("20ctV1","nameImage ==> " + nameImageString);

//            Upload Image to server
            StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy
                    .Builder().permitAll().build();

            StrictMode.setThreadPolicy(threadPolicy);
            File file = new File(pathImageString);

            MyConstanst myConstanst = new MyConstanst();
            FTPClient ftpClient = new FTPClient();


            try {
                ftpClient.connect(myConstanst.getHost(), 21);
                ftpClient.login(myConstanst.getUser(), myConstanst.getPassword());
                ftpClient.setType(FTPClient.TYPE_BINARY);
                ftpClient.changeDirectory("Tikk");
                ftpClient.upload(file,new uploadListener());

                AddNewUser addNewUser = new AddNewUser(getActivity());//add value to sql
                addNewUser.execute(nameString, emaiString, passwordString,
                        myConstanst.getPrefixString() +nameImageString, myConstanst.getUrlAddUserString()
                );

                String result = addNewUser.get();
                Log.d("20ctV1", "result ==> " + result);

                if (Boolean.parseBoolean(result)) {
                    getActivity().getSupportFragmentManager().popBackStack();

                } else {
                    myAlert.normalDialog("connot upload" ,"please try again");
                }

            } catch (Exception e) {
                Log.d("3octV1","E ="+ e.toString());
                try {
                    ftpClient.disconnect(true);

                } catch (Exception el) {
                    Log.d("3octV1","E1 ="+ el.toString());
                    el.printStackTrace();
                }//try2
            }// try1


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
