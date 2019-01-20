package com.hello.khushboo.messaging.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hello.khushboo.messaging.Adapter.UserAdapter;
import com.hello.khushboo.messaging.Model.User;
import com.hello.khushboo.messaging.R;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private final String TAG="UsersFragment";

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUser;

    EditText search_users;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUser=new ArrayList<>();
        readUsers();
        search_users=(EditText)view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                search_users(charSequence.toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;

    }

    private void search_users(String s){
        final FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf88f");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    assert fuser != null;
                    assert user != null;

                    if(!user.getId().equals(fuser.getUid())){
                        mUser.add(user);
                    }


                }

                userAdapter =new UserAdapter(getContext(),mUser,false);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readUsers(){
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(search_users.getText().toString().equals("")) {

                    mUser.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        assert user != null;
                        assert firebaseUser != null;


                        if (user.getId().equals(firebaseUser.getUid())) {
                            //mUser.add(user);
                            Log.d(TAG, "Same user");
                        } else {
                            mUser.add(user);
                        }
                    }

                    userAdapter = new UserAdapter(getContext(), mUser, false);
                    recyclerView.setAdapter(userAdapter);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
