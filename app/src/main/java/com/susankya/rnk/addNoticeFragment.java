package com.susankya.rnk;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class addNoticeFragment extends android.support.v4.app.Fragment implements FragmentCodes {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";

    @BindView(R.id.spinnercategory_fixed)
    Spinner spinnerCategory;
    @BindView(R.id.cardViewAttachment)
    CardView cardViewOfAttachment;
    @BindView(R.id.fileTypeImage)
    ImageView attachmentImage;
    @BindView(R.id.filename)
    TextView attachmentText;
    @BindView(R.id.floating_add_button)
    FloatingActionButton fab;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.description)
    EditText description;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 125;
    private String bsDate;
    java.util.Date ad;
    private static final int PICK_IMAGE = 2, PICKFILE = 3;
    private String mParam1, cat, description_text, current_date, current_time, link, Date, titleText;
    private String mParam2, noticeNumber = "0";
    private int categoryPos;
    private boolean isEdit = false;
    private String filePath;
    private boolean hasAttachment = false;
    private DateConverter dateconverter;

    public static addNoticeFragment newInstance(String param1, String param2, boolean isEdit, String noticeNum, String category) {
        addNoticeFragment fragment = new addNoticeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putBoolean(ARG_PARAM3, isEdit);
        args.putString(ARG_PARAM4, noticeNum);
        args.putString(ARG_PARAM5, category);
        fragment.setArguments(args);
        return fragment;
    }

    public addNoticeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().invalidateOptionsMenu();
        if (!isEdit)
            inflater.inflate(R.menu.add_notice_menu, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE:
                if (resultCode == getActivity().RESULT_OK) {
                    hasAttachment = true;
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    filePath = CompressImage.getPathPlus(getActivity(), selectedImage);
                    manageCardView(filePath.substring(filePath.lastIndexOf("/") + 1), false);
                    boolean isImage = false;
                    String[] extensions = new String[]{"jpg", "png", "jpeg", "gif"};
                    for (String ext : extensions) {
                        if (filePath.endsWith(ext))
                            isImage = true;
                    }
                    if (isImage) {
                        CompressImage compressImage = new CompressImage(getActivity(), selectedImage);
                        filePath = compressImage.getFinalFilename();
                        //Log.d("TAG1", "onActivityResult: "+compressImage.getFinalFilename());
                        manageCardView(filePath.substring(filePath.lastIndexOf("/") + 1), true);
                    }
                    //Log.d("TAG", "onActivityResult:edited"+filePath);
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_attachment:
                checkPermission();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);//title
            mParam2 = getArguments().getString(ARG_PARAM2);//description
            isEdit = getArguments().getBoolean(ARG_PARAM3);//isEditableMode
            noticeNumber = getArguments().getString(ARG_PARAM4);//NoticeNum
            if (isEdit)
                categoryPos = categoryCheck(getArguments().getString(ARG_PARAM5));
        } else {
            mParam1 = mParam2 = "";
            isEdit = false;
            noticeNumber = "0";
        }
    }

    private int categoryCheck(String text) {
        int cat = 0;
        String[] categoryList = getActivity().getResources().getStringArray(R.array.category_list);
        for (int i = 0; i < categoryList.length; i++) {
            if (text.equalsIgnoreCase(categoryList[i]))
                cat = i;
        }
        return cat;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
//        if(isEdit)getActivity().setTitle("Add Notice"); else getActivity().setTitle("Edit Notice");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_notice, container, false);
        ButterKnife.bind(this, v);
        if (isEdit) {
//            cardViewOfAttachment.setVisibility(View.VISIBLE);
            getActivity().setTitle("Edit Notice");
        } else {
            getActivity().setTitle("Add Notice");
        }

        title = v.findViewById(R.id.title);
        title.setText(mParam1);
        description.setText(mParam2);
        String[] array = getActivity().getResources().getStringArray(R.array.category_list);
        ArrayAdapter<String> cs = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, array);
        spinnerCategory.setAdapter(cs);

        if (isEdit) {
            title.setEnabled(false);
            spinnerCategory.setEnabled(false);
            spinnerCategory.setSelection(categoryPos);
        }

        setHasOptionsMenu(true);
        description = v.findViewById(R.id.description);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat = spinnerCategory.getSelectedItem().toString().trim();
                description_text = description.getText().toString().trim();
                titleText = title.getText().toString().trim();
                if (description_text.isEmpty() || titleText.isEmpty())
                    Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_SHORT).show();
                else {
                    final Calendar c = Calendar.getInstance();
                    final SimpleDateFormat d1 = new SimpleDateFormat("yyyy MMM dd");
                    final SimpleDateFormat d2 = new SimpleDateFormat("hh:mm:ss a");
                    Date = d1.format(c.getTime());
                    current_time = d2.format(c.getTime());
                    //final DateConverter dc=new DateConverter();
                    AddNotice();
                }
            }
        });
        return v;
    }

    private void manageCardView(String filename, boolean isImage) {
        cardViewOfAttachment.setVisibility(View.VISIBLE);
        description_text = filename;
        if (isImage)
            attachmentImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_image));
        else
            attachmentImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_file));
        attachmentText.setText(filename);
    }

    private void AddNotice() {
        titleText = title.getText().toString();
        link = NEW_HOST + "AddNotice.php";
        if (hasAttachment) {
            if (description_text.isEmpty()) {
                description_text = filePath.substring(filePath.lastIndexOf("/" + 1));
            }
            //Log.d("TAG", "AddNotice: "+titleText);
            File file = new File(filePath);

            if ((file.length() / 1024) > 5120) {
                Toast.makeText(getActivity(), "Maximum file size limit is 5 MB.", Toast.LENGTH_SHORT).show();
            } else {
                NewFileUploader fileUploader = new NewFileUploader(filePath, "file", link, new String[]{"cmdxxx", "college_sn", "data", "category", "date", "time", "title"}, new String[]{"2568wwexve", UtilitiesAdi.giveMeSN(getActivity(), Utilities.getDatabaseName(getActivity())), description_text, cat, Date, current_time, titleText}, getActivity());
                fileUploader.setFileUploadedListener(new NewFileUploader.FileUploadedListener() {
                    @Override
                    public void OnFileUploaded(String result) {
                        if (result.equals("1")) {
                            cardViewOfAttachment.setVisibility(View.GONE);
                            hasAttachment = false;
                            Toast.makeText(getActivity().getApplicationContext(), "Notice added successfully", Toast.LENGTH_SHORT).show();
                            title.setText("");
                            description.setText("");
                        } else
                            Toast.makeText(getActivity().getApplicationContext(), "Failed to add notice" + result, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            if (!isEdit) {    //Log.d("TAG", "onHandleIntent: 1"+Utilities.getDatabaseName(getActivity()));
                new PhpConnect(link, "Publishing notice...", getActivity(), 1, new String[]{CMDXXX, description_text, cat, "65", Date, current_time, titleText}, new String[]{"cmdxxx", "data", "category", "college_sn", "date", "time", "title"}).setListener(new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        // Toast.makeText(getActivity(),res,Toast.LENGTH_SHORT).show();
                        Log.d("fatal", "Notice" + res);
                        if (res.equals("1")) {
                            Toast.makeText(getActivity().getApplicationContext(), "Notice added successfully", Toast.LENGTH_SHORT).show();
                            description.setText("");
                            title.setText("");
                        } else
                            Toast.makeText(getActivity().getApplicationContext(), "Failed to add notice", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                //Log.d("TAG", "onHandleIntent: 2"+Utilities.getDatabaseName(getActivity()));
                String editLink = FragmentCodes.NEW_HOST + "Edit/editNotice.php";
                new PhpConnect(editLink, "Changing notice...", getActivity(), 1, new String[]{CMDXXX, noticeNumber, description_text}
                        , new String[]{"cmdxxx", "notice_number", "notice"}).setListener(new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        Log.d("response", "" + res);
                        if (res.equals("1")) {
                            Toast.makeText(getActivity().getApplicationContext(), "Notice edited successfully, swipe down to refresh.", Toast.LENGTH_SHORT).show();
                            description.setText("");
                            title.setText("");
                            getActivity().finish();
                        } else
                            Toast.makeText(getActivity().getApplicationContext(), "Failed to edit notice", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 23) {
            int hasReadPermission = getActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
            showFileChooser();
        } else {
            showFileChooser();
        }
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra("CONTENT_TYPE", "*/*");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(intent, PICKFILE);
    }

   /* public String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(
                contentUri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return  filePath;
    }*/
}
