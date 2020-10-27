package com.kuruvatech.quickbuy.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kuruvatech.quickbuy.R;
import com.kuruvatech.quickbuy.model.Address;
import com.kuruvatech.quickbuy.model.FavouriteAddress;
import com.kuruvatech.quickbuy.utils.Constants;
import com.kuruvatech.quickbuy.utils.SessionManager;


/**
 * Created by dganeshappa on 7/26/2016.
 */
public class AddressListAdapater extends BaseAdapter {
    Activity mContext;
    int mLayoutResID;
    List<android.location.Address> mAddresses;
    int pos;
    SessionManager session;
    int del_pos;
    Button save;
    EditText editTagLabel,editCity,editHouseNo,editAreaName,editLandmark,editAddress;
    ArrayList<FavouriteAddress> mFavouriteAddressArrayList;
   public void alertMessage(String message) {
        DialogInterface.OnClickListener dialogClickListeneryesno = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message).setNeutralButton("Ok", dialogClickListeneryesno)
                .setIcon(R.drawable.ic_action_about).show();

    }

    public void setmFavouriteAddressArrayList(ArrayList<FavouriteAddress> favouriteAddressArrayList) {
        mFavouriteAddressArrayList.clear();
        mFavouriteAddressArrayList = (ArrayList<FavouriteAddress>) favouriteAddressArrayList.clone();

        notifyDataSetChanged();
    }


    public AddressListAdapater(Activity context, int layoutResourceID,
                               ArrayList<FavouriteAddress> favouriteAddressArrayList) {
        mContext = context;
        mLayoutResID = layoutResourceID;
        mFavouriteAddressArrayList = favouriteAddressArrayList;

    }

    @Override
    public int getCount() {
        return mFavouriteAddressArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        pos=position;
        session=new SessionManager(mContext);
        ItemHolder itemHolder = new ItemHolder();
        if(view == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            view = inflater.inflate(mLayoutResID, parent, false);
            itemHolder.addressdetail = (TextView)view.findViewById(R.id.address_detail);
            itemHolder.addressLabel = (TextView)view.findViewById(R.id.address_label);
            itemHolder.editButton =(ImageView) view.findViewById(R.id.edit_button);
            itemHolder.deleteButton =(ImageView) view.findViewById(R.id.delete_button);
            itemHolder.editButton.setTag(position);
            itemHolder.deleteButton.setTag(position);
            view.setTag(itemHolder);
         }
        else{
            itemHolder = (ItemHolder) view.getTag();
        }
        itemHolder.addressLabel.setText(String.valueOf(mFavouriteAddressArrayList.get(position).getLabel()));
        itemHolder.addressdetail.setText(String.valueOf(mFavouriteAddressArrayList.get(position).getAddress().toString()));
       final String mes= itemHolder.addressLabel.getText().toString();
        itemHolder.deleteButton.setOnClickListener(new View.OnClickListener() {

                                         @Override
                                          public void onClick(View v) {
                                             final int position = (Integer) v.getTag();
                                             android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);

                                             builder.setTitle("Fruity");
                                             builder.setMessage("Are you sure you want to delete this Address?");
                                             builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int whch) {
                                                     del_pos=position;
                                                     String order_url =  Constants.DELETE_ADDRESS;
                                                     order_url= order_url+session.getKeyPhone()+"/"+mFavouriteAddressArrayList.get(position).getLabel().trim();

                                                     new DeleteJSONAsyncTask().execute(order_url);

                                                 }
                                             });
                                             builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int whch) {

                                                 }
                                             });
                                             builder.show();
//                                              int posi = (Integer) v.getTag();
//                                              del_pos=posi;
//                                              String order_url =  Constants.DELETE_ADDRESS;
//                                              order_url= order_url+session.getKeyPhone()+"/"+mFavouriteAddressArrayList.get(posi).getLabel();
//
//                                              new DeleteJSONAsyncTask().execute(order_url);

                                          }
                                      }
        );
        itemHolder.editButton.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         int position = (Integer) v.getTag();
                                                         del_pos=position;

                                                         android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);

                                                         builder.setTitle("Fruity");
                                                         builder.setMessage("Are you sure you want to Edit this Address?");
                                                         builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                                             @Override
                                                             public void onClick(DialogInterface dialog, int whch) {
                                                                 final Dialog dialog1 = new Dialog(mContext);
                                                                 dialog1.setContentView(R.layout.add_address_layout);
                                                                 save = (Button) dialog1.findViewById(R.id.saveAddressbutton);
                                                                 editCity = (EditText) dialog1.findViewById(R.id.orderDetailEmail);
                                                                 editHouseNo = (EditText) dialog1.findViewById(R.id.orderDetailAddress_house_no);
                                                                 editAreaName = (EditText) dialog1.findViewById(R.id.orderDetailAddress_areaname);
                                                                 editLandmark = (EditText) dialog1.findViewById(R.id.orderDetailAddress_landmark);
                                                                 editAddress = (EditText) dialog1.findViewById(R.id.orderDetailAddress_address);
                                                                 editTagLabel = (EditText) dialog1.findViewById(R.id.tag_address_label);
                                                                 editCity.setText(mFavouriteAddressArrayList.get(del_pos).getAddress().getCity());
                                                                 editHouseNo.setText(mFavouriteAddressArrayList.get(del_pos).getAddress().getAddressLine1());
                                                                 editLandmark.setText(mFavouriteAddressArrayList.get(del_pos).getAddress().getLandMark());
                                                                 editAreaName.setText(mFavouriteAddressArrayList.get(del_pos).getAddress().getAreaName());
                                                                 editAddress.setText(mFavouriteAddressArrayList.get(del_pos).getAddress().getAddressLine2());
                                                                 editTagLabel.setText(mFavouriteAddressArrayList.get(del_pos).getLabel());

                                                                 dialog1.show();
                                                                 save.setOnClickListener(new View.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(View v) {

                                                                         String house = editHouseNo.getText().toString();
                                                                         String addresss = editAddress.getText().toString();
                                                                         String areaname = editAreaName.getText().toString();
                                                                         String landmark = editLandmark.getText().toString();
                                                                         String tagLabel = editTagLabel.getText().toString();
                                                                         if (house.trim().length() == 0) {
                                                                             //Toast.makeText(getApplicationContext(), "Enter House No or Flat No ", Toast.LENGTH_LONG).show();
                                                                             alertMessage("Enter House or Flat No ");
                                                                         } else if (addresss.trim().length() <= 0) {
                                                                             //Toast.makeText(getApplicationContext(), "Enter areaname ", Toast.LENGTH_LONG).show();
                                                                             alertMessage("Enter adress ");
                                                                         } else if (areaname.trim().length() <= 0) {
                                                                             //Toast.makeText(getApplicationContext(), "Enter Address ", Toast.LENGTH_LONG).show();
                                                                             alertMessage("Enter Areaname ");
                                                                         } else if (landmark.trim().length() <= 0) {
                                                                             //Toast.makeText(getApplicationContext(), "Enter Landmark/locality ", Toast.LENGTH_LONG).show();
                                                                             alertMessage("Enter Landmark/locality ");
                                                                         } else if (tagLabel.trim().length() <= 0) {
                                                                             //Toast.makeText(getApplicationContext(), "Enter City ", Toast.LENGTH_LONG).show();
                                                                             alertMessage("Enter lable for this address");
                                                                         } else {
                                                                             Address address;
                                                                             address = mFavouriteAddressArrayList.get(del_pos).getAddress();
                                                                             address.setAreaName(editAreaName.getText().toString());
                                                                             address.setLandMark(editLandmark.getText().toString());
                                                                             address.setAddressLine1(editHouseNo.getText().toString());
                                                                             address.setAddressLine2(editAddress.getText().toString());
                                                                             address.setCity(editCity.getText().toString());
                                                                             if (mAddresses != null) {
                                                                                 address.setZip(mAddresses.get(0).getPostalCode());
                                                                                 address.setLatitude(String.valueOf(mAddresses.get(0).getLatitude()));
                                                                                 address.setLongitude(String.valueOf(mAddresses.get(0).getLongitude()));
                                                                             }

                                                                             mFavouriteAddressArrayList.get(del_pos).setLabel(editTagLabel.getText().toString());
                                                                             mFavouriteAddressArrayList.get(del_pos).setAddress(address);
                                                                             SessionManager session = new SessionManager(mContext);
                                                                             session.setFavoutrateAddress(mFavouriteAddressArrayList.get(del_pos), del_pos);

                                                                             dialog1.cancel();

                                                                         }

                                                                     }

                                                                     ;

                                                                 });
                                                             }
                                                         });
                                                         builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                                                             @Override
                                                             public void onClick(DialogInterface dialog, int whch) {

                                                             }
                                                         });
                                                         builder.show();
                                                     }
                                                 });


        return view;
    }
    private static class ItemHolder {
        TextView addressLabel;
        TextView addressdetail;
        ImageView editButton,deleteButton;
  }

    public  class DeleteJSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        //        ListView mListView;
//        Activity mContex;
        public  DeleteJSONAsyncTask()
        {
//            this.mListView=gview;
//            this.mContex=contex;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpDelete request = new HttpDelete(urls[0]);
                request.addHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.addHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(request);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {


                    return true;
                }

                //------------------>>

            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();

            if(result == true){
                mFavouriteAddressArrayList.remove(del_pos);
                session.removeFavourateAddressList(del_pos);
                notifyDataSetChanged();
            }
            if (result == false)
                Toast.makeText(mContext, "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }
}