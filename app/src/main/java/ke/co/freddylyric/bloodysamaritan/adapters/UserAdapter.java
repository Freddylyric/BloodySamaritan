package ke.co.freddylyric.bloodysamaritan.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ke.co.freddylyric.bloodysamaritan.R;
import ke.co.freddylyric.bloodysamaritan.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{


    private TextView drive;
    private final Context context;
    private final List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;

    }

    @NonNull
     @Override
     public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view =LayoutInflater.from(context).inflate(
                 R.layout.user_displayed_layout, parent, false);
         return new ViewHolder(view);


     }

     @Override
     public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user = userList.get(position);


        holder.type.setText(user.getType());

        //hide email option from donors
        //if (user.getType().equals("donor")){
           // holder.callNow.setVisibility((View.VISIBLE));

       //}

         holder.userName.setText(user.getName());
         holder.userEmail.setText(user.getEmail());
         holder.phoneNumber.setText(user.getPhoneNumber());
         holder.bloodGroup.setText(user.getBloodGroup());
         holder.region.setText(user.getRegion());

         Glide.with(context).load(user.getProfilepictureurl()).into(holder.userProfileImage);

         holder.callNow.setOnClickListener(view -> {
             String call = user.getPhoneNumber();
             Intent intent = new Intent(Intent.ACTION_DIAL);
             intent.setData(Uri.parse("tel:" + call));
             context.startActivity(intent);
         });

     }

     @Override
     public int getItemCount() {
         return userList.size();
     }

     public static class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView userProfileImage;
        public TextView type, userName, userEmail, phoneNumber, bloodGroup, region;
        public Button callNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfileImage =itemView.findViewById(R.id.userProfileImage);
            type =itemView.findViewById(R.id.type);
            userName =itemView.findViewById(R.id.userName);
            userEmail =itemView.findViewById(R.id.userEmail);
            phoneNumber =itemView.findViewById(R.id.phoneNumber);
            bloodGroup =itemView.findViewById(R.id.bloodGroup);
            region =itemView.findViewById(R.id.region);
            callNow =itemView.findViewById(R.id.callNow);


        }
    }
}
