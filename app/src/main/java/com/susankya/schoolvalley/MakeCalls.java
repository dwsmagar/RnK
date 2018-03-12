package com.susankya.schoolvalley;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MakeCalls extends android.support.v4.app.Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public static final int PHYSICS=1;
    public static final int CHEMISTRY=2;
    public static final int MATHS=3;
    public static final int BIOLOGY=4;
    public static final int ENGLISH=5;
    public static final int NEPALI=6;
    public static final int COMPUTER=7;
    private ViewGroup mContainerView;
    private String[] names={"Aditya","Sanjog"};
    private String[] numbers={"+9779862027999","+9779842493334"};
    public static String BHIM_KAFLE="https://facebook.com/bhim.kafle.148";
    public static String BABURAM_GURAGAIN="https://facebook.com/baburam.guragain";
    public static String NILAM_ADHIKARI="https://facebook.com/adhikarinilam";
    public static String UJJATA_THAPALIYA="https://facebook.com/ujjata.thapaliya";
    public static String SUMAN_GURAGAI="https://facebook.com/sguragai1";
    public static String PADAM_BIKRAM_ADHIKARI="https://facebook.com/padam.bikramadhikari";
    public static String ABHAY_KARN="https://facebook.com/abhay.karn.79";
    public static String KESHAV_MASKEY="https://facebook.com/keshav.maskey";
    public static String RABIN_BHATTARAI="https://facebook.com/rabin.bhattarai.315"; // LIBRARIAN
    public static String UMENDRA_BILASH_LUITEL="https://facebook.com/bilashsir";
    public static String RAJESH_KARKI="https://facebook.com/rajesh.karki12";
    public static String KRISHNA_WASTI="https://facebook.com/krishnawasti";
    public static String KEDAR_PRASAD_LOHANI="https://facebook.com/kedarprasad.lohani";
    public static String UPENDRA_MEHTA="https://facebook.com/upendra.mehta.94";
    public static String BHABINDRA_NIROULA="https://facebook.com/bhabindra.niroula";
    public static String BIKASH_KHANAL="https://facebook.com/bikash.khanal.167";
    public static String BHARAT_DAHAL="https://facebook.com/bharat.dahal.35";
    public static String ROSHAN_LUITEL="https://m.facebook.com/roshan.luitel.9";
    public static String MADAN_SEDAI="https://facebook.com/madan.sedai.7"; //ARNIKO KO D.I.
    private LinearLayout listView_physics,listView_chemistry,listView_maths,listView_biology,listView_nepali,listView_english,listView_computer;
    private Button physics_btn, chemistry_btn, maths_btn, biology_btn,english_btn,nepali_btn,computer_btn;
   private ArrayList<TeacherProfile> physics,chemistry,maths,biology,english,computer,nepali;
    private TeacherProfile sgp,ubl,bk,bkm,lp,rk,skj,bl,gsc,sny,pba,aks;
    public static MakeCalls newInstance(String param1, String param2) {
        MakeCalls fragment = new MakeCalls();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MakeCalls() {
        // Required empty public constructor
    }
    //1=phy,2=che,3=maths,4=bio
private void fillInfo(String name,String url,String phone,int subject){
   TeacherProfile tp=new TeacherProfile();
    tp.setName(name);
    tp.setUrl(url);
    tp.setPhoneNumber(phone);
    switch (subject){
        case 1:
            physics.add(tp);
            break;
        case 2:
            chemistry.add(tp);
            break;
        case 3:
            maths.add(tp);
            break;
        case 4:
            biology.add(tp);
            break;
        case COMPUTER:
            computer.add(tp);
            break;
        case ENGLISH:
            english.add(tp);
            break;
        case NEPALI:
            nepali.add(tp);
            break;


    }
}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        physics=new ArrayList<>();
        chemistry=new ArrayList<>();
        maths=new ArrayList<>();
        biology=new ArrayList<>();
        english=new ArrayList<>();
        nepali=new ArrayList<>();
        computer=new ArrayList<>();
        //1=phy,2=che,3=maths,4=bio
      fillInfo("Baburam Guragain",BABURAM_GURAGAIN,"",MATHS);
      fillInfo("Ujjata Thapaliya",UJJATA_THAPALIYA,"",CHEMISTRY);
      fillInfo("Nilam Adhikari",NILAM_ADHIKARI,"9842034495",PHYSICS);
      fillInfo("Er. Abhay Karn",ABHAY_KARN,"",COMPUTER);

      fillInfo("Bhabindra Niroula",BHABINDRA_NIROULA,"9842134558",BIOLOGY);
        fillInfo("Bharat Dahal",BHARAT_DAHAL,"9852032910",ENGLISH);
      fillInfo("Suman Guragai",SUMAN_GURAGAI,"",1);
      fillInfo("Bilash Luitel",UMENDRA_BILASH_LUITEL,"9842021435",1);
        fillInfo("Keshav Maskey",KESHAV_MASKEY,"",MATHS);
        fillInfo("Rajesh Karki",RAJESH_KARKI,"9842050332",CHEMISTRY);
       fillInfo("Krishna Wasti",KRISHNA_WASTI,"",NEPALI);
        fillInfo("Kedar Prasad Lohani",KEDAR_PRASAD_LOHANI,"",PHYSICS);
        fillInfo("Upendra Mehta",UPENDRA_MEHTA,"",PHYSICS);
      fillInfo("Roshan Luitel",ROSHAN_LUITEL,"",PHYSICS);
      fillInfo("Bikash Khanal",BIKASH_KHANAL,"",4);//sgp,ubl,bk,bkm,lp,rk,skj,bl,gsc,sny,pba,aks;
      fillInfo("Bhim Kafle",BHIM_KAFLE,"",3);
      fillInfo("Laxmi Adhikari","","9842049710",3);
      fillInfo("Sailesh Kumar Jha","","9842032804",3);
      fillInfo("Bhola Luitel","","",2);
      fillInfo("Dr. Ghanshyam Shriwastav","","",2);
      fillInfo("Shiv Narayan Yadav","","",3);
      fillInfo("Padam Bikram Adhikari",PADAM_BIKRAM_ADHIKARI,"",1);
      fillInfo("Amit Kumar Sarkar", "", "", 2);
        fillInfo("Er. Archana Karn","","",COMPUTER);
        fillInfo("Saran Gautam","","",ENGLISH);
        fillInfo("Sita Dahal","","",ENGLISH);
        fillInfo("Arun Koirala","","",ENGLISH);
        fillInfo("Amina Joshi","","",ENGLISH);
        fillInfo("Arun Mukharjee","","",ENGLISH);
        fillInfo("Sita Ram Gupta","","",CHEMISTRY);
        fillInfo("Sashi Chaudhary","","",CHEMISTRY);
        fillInfo("Indira Pokhrel","","",BIOLOGY);
        fillInfo("Dr. Mohan Jee Thakur","","",BIOLOGY);
        fillInfo("Sujata Koirala","","",BIOLOGY);
        fillInfo("Ram Guragain","","",BIOLOGY);
        fillInfo("Nirmala Kafle","","",BIOLOGY);
        fillInfo("Tankanath Koirala","","",NEPALI);
        fillInfo("Ramnath Sharma","","",NEPALI);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_make_calls, container, false);
    listView_physics =(LinearLayout)v.findViewById(R.id.list_physics);
     listView_chemistry=(LinearLayout)v.findViewById(R.id.list_chemistry);
    listView_maths =(LinearLayout)v.findViewById(R.id.list_maths);
        listView_biology=(LinearLayout)v.findViewById(R.id.list_biology);
        listView_computer=(LinearLayout)v.findViewById(R.id.list_computer);
        listView_nepali=(LinearLayout)v.findViewById(R.id.list_nepali);
        listView_english=(LinearLayout)v.findViewById(R.id.list_english);
        physics_btn =(Button)v.findViewById(R.id.physics);
          chemistry_btn =(Button)v.findViewById(R.id.chemistry);
          maths_btn =(Button)v.findViewById(R.id.mathematics);
          biology_btn =(Button)v.findViewById(R.id.biology);
        english_btn=(Button)v.findViewById(R.id.english);
        nepali_btn=(Button)v.findViewById(R.id.nepali);
        computer_btn=(Button)v.findViewById(R.id.computer);

        english_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView_english.isShown())
                    listView_english.setVisibility(View.GONE);
                else
                    listView_english.setVisibility(View.VISIBLE);
            }
        });
        nepali_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView_nepali.isShown())
                    listView_nepali.setVisibility(View.GONE);
                else
                    listView_nepali.setVisibility(View.VISIBLE);
            }
        });

        computer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView_computer.isShown())
                    listView_computer.setVisibility(View.GONE);
                else
                    listView_computer.setVisibility(View.VISIBLE);
            }
        });
          physics_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView_physics.isShown())
                    listView_physics.setVisibility(View.GONE);
                else
                    listView_physics.setVisibility(View.VISIBLE);
            }
        });
        chemistry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( listView_chemistry.isShown())
                    listView_chemistry.setVisibility(View.GONE);
                else
                    listView_chemistry.setVisibility(View.VISIBLE);
            }
        });
        maths_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView_maths.isShown())
                    listView_maths.setVisibility(View.GONE);
                else
                    listView_maths.setVisibility(View.VISIBLE);
            }
        });
        biology_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView_biology.isShown())
                    listView_biology.setVisibility(View.GONE);
                else
                    listView_biology.setVisibility(View.VISIBLE);
            }
        });
        setAdapter(v,R.id.list_physics,physics);
        setAdapter(v,R.id.list_chemistry,chemistry);
        setAdapter(v,R.id.list_maths,maths);

        setAdapter(v,R.id.list_biology,biology);
        setAdapter(v,R.id.list_english,english);
        setAdapter(v,R.id.list_nepali,nepali);

        setAdapter(v,R.id.list_computer,computer);

            return v;
    }

private void setAdapter(View view,int id,ArrayList<TeacherProfile> tp){
    mContainerView = (ViewGroup)view.findViewById(id);

    for(int i=tp.size();i>0;i--){
        final ViewGroup v = (ViewGroup) LayoutInflater.from(getActivity()).inflate(
                R.layout.number_list_item, mContainerView, false);
        TextView tv0=(TextView)v.findViewById(R.id.name);
        ImageView im=(ImageView)v.findViewById(R.id.call_button);
        ImageView url=(ImageView)v.findViewById(R.id.fb_url);
      final TeacherProfile pro=tp.get(i-1);
      tv0.setText(pro.getName());
try {
    if (pro.getPhoneNumber().equals(""))
        im.setVisibility(View.INVISIBLE);
    if (pro.getUrl().equals(""))
        url.setVisibility(View.INVISIBLE);
}catch(Exception e){
    //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
}
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+pro.getPhoneNumber()));
                startActivity(intent);
            }
        });
        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(pro.getUrl()));
                startActivity(i);
            }
        });
        mContainerView.addView(v, 0);
    }
}

    private class TeacherProfile{
        private String name;
        private String url;
        private String phoneNumber;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
       public String getPhoneNumber() {
            return phoneNumber;
        }
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        }
}