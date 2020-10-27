package com.kuruvatech.quickbuy.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.kuruvatech.quickbuy.R;
import com.kuruvatech.quickbuy.model.MenuAdapter;
import com.kuruvatech.quickbuy.utils.Constants;

public class ProductAdapter extends BaseAdapter {
    Activity con;
    Typeface cr;
    int layoutResID;
    int pos;
    int increment=0;

    int j=0;
    int i=0;
    String urlImg;
    private Integer count;
    public int totalCount ;
    public int totalCost;
    ImageView img;
    PlusMinusButtonListener mListener;
    public ArrayList<MenuAdapter> getmMenulist() {
        return mMenulist;
    }

    public void setmMenulist(ArrayList<MenuAdapter> mMenulist) {
        this.mMenulist = mMenulist;
    }

    ArrayList<MenuAdapter> mMenulist;
    // PreferenceManager mpref;
    public ProductAdapter(Activity context, int layoutResourceID,
                          ArrayList<MenuAdapter> menuList,int i) {

        con = context;
        layoutResID = layoutResourceID;
        mMenulist =menuList;
        totalCount = 0;
        totalCost = 0;
        urlImg = Constants.ABOUT_US_URL;
        this.i=i;


    }
    public ProductAdapter(Activity context, int layoutResourceID,
                          ArrayList<MenuAdapter> menuList) {

        con = context;
        layoutResID = layoutResourceID;
        mMenulist =menuList;
        totalCount = 0;
        totalCost = 0;
        urlImg = Constants.ABOUT_US_URL;

        // this.cr=cr;

    }
    public void setListener(PlusMinusButtonListener listener)
    {
        mListener = listener;
    }
    @Override
    public int getCount() {
        return mMenulist.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        CityItemHolder itemHolder;

        //  mpref= PreferenceManager.instance(con);
        //  LocationListNameModel city = getItem(position);

        itemHolder = new CityItemHolder();
        if(view==null){
            LayoutInflater inflater = ((Activity)con).getLayoutInflater();
            view = inflater.inflate(layoutResID, parent, false);


            itemHolder = new CityItemHolder();
            itemHolder.city= (TextView) view.findViewById(R.id.product_detail_name);
            itemHolder.itemDescription=(TextView) view.findViewById(R.id.itemdescription);
            itemHolder.price_value = (TextView) view.findViewById(R.id.approx_price_val);

            view.setTag(itemHolder);
        }else{
            itemHolder = (CityItemHolder) view.getTag();
        }

        //   itemHolder.city.setTypeface(cr);
        itemHolder.mAddImg = (ImageView) view.findViewById(R.id.add_btn);
        itemHolder.mAddImg.setTag(position);
        itemHolder.mSubImg = (ImageView) view.findViewById(R.id.sub_btn);

        itemHolder.mItemImg=(ImageView) view.findViewById(R.id.product_detail_image_view);


        itemHolder.mSubImg.setTag(position);
        itemHolder.city.setText(mMenulist.get(position).getName());
        itemHolder.logo=mMenulist.get(position).getLogo();
     try {
         Picasso.with(con)
                 .load(mMenulist.get(position).getLogo())
                 .into(itemHolder.mItemImg);
     }catch(IllegalArgumentException e)
     {
         e.printStackTrace();
     }
        itemHolder.itemDescription.setText(mMenulist.get(position).getItemDescription());



        itemHolder.price_value.setText(new String("₹ ").concat(String.valueOf(mMenulist.get(position).getPrice())));

        itemHolder.mValue = (TextView) view.findViewById(R.id.add_sub_val);
        itemHolder.mValue.setText(String.valueOf(mMenulist.get(position).getNo_of_order()));


        final CityItemHolder finalItemHolder1 = itemHolder;
        if (i == 1) {
            increment=5;
              }else {
            increment=1;
        }
        itemHolder.mAddImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    totalCount = totalCount + increment;

                    v.getTag();
                    int posi = (Integer) v.getTag();
                    View parentRow = (View) v.getParent();
                    ListView listView = (ListView) parentRow.getParent();

                    final int posforadd = listView.getPositionForView(parentRow);
                    // Toast.makeText(getApplicationContext(),posi+""+posforadd,Toast.LENGTH_LONG).show();
                    TextView textName = (TextView) parentRow.findViewById(R.id.product_detail_name);
                    TextView tiew = (TextView) parentRow.findViewById(R.id.add_sub_val);
                    tiew.setVisibility(View.VISIBLE);
                    String quantity = tiew.getText().toString();
                    count = new Integer(quantity);
                    Log.e("add" + count, "^^^^^^^");
                    int val = count + increment;
                    String sVal = "" + val;
                    tiew.setText(sVal);
                    ImageView btn = (ImageView) parentRow.findViewById(R.id.add_btn);
                    //   btn.setTextColor(Color.parseColor("#07c2b1"));
                    mMenulist.get(posi).setNo_of_order(val);
                    totalCost = totalCost + mMenulist.get(posi).getPrice();
                    mListener.buttonClicked(pos, val);
                /*String productname = textName.getText().toString();
                int productID=  productList.get(posforadd).getVendorProduct().getId();
                for(int j=0;j<=mProductModel.getSuccess().size();j++){
                    if(productID==mProductModel.getSuccess().get(j).getVendorProduct().getId()){

                        TextView subtn = (TextView) parentRow.findViewById(R.id.sub_btn);
                        subtn.setVisibility(View.VISIBLE);
                        subtn.setTextColor(Color.parseColor("#8A8A8A"));
                        //  countnumber2++;

                        //   Log.e("ival" + j, "%%%%%%%%%%");

                        AddToJsonProductList(val, j);


                        return;
                    }
                }*/


                }
            });


        final CityItemHolder finalItemHolder = itemHolder;
        itemHolder.mSubImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.getTag();
                int posi = (Integer) v.getTag();
                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();

                final int posforsub = listView.getPositionForView(parentRow);
                // Toast.makeText(getApplicationContext(),posi+""+posforsub,Toast.LENGTH_LONG).show();
                TextView textName = (TextView) parentRow.findViewById(R.id.product_detail_name);
                TextView tiew = (TextView) parentRow.findViewById(R.id.add_sub_val);
                String quantity = tiew.getText().toString();
                count = new Integer(quantity);
                Log.e("sub" + count, "^^^^^^^");
                if (count > 0) {
                    totalCount =  totalCount - increment;
                    int val = count - increment;
                    // Toast.makeText(getApplicationContext(),""+val,Toast.LENGTH_LONG).show();
                    String sVal = "" + val;
                    tiew.setText(sVal);
                    ImageView subbtn = (ImageView) parentRow.findViewById(R.id.sub_btn);
                 //   subbtn.setTextColor(Color.parseColor("#07c2b1"));
                    ImageView addbtn = (ImageView) parentRow.findViewById(R.id.add_btn);
                  //  addbtn.setTextColor(Color.parseColor("#8A8A8A"));
                    String productname = textName.getText().toString();
                    mMenulist.get(posi).setNo_of_order(val);
                    totalCost = totalCost  -mMenulist.get(posi).getPrice();
                    mListener.buttonClicked(pos,val);

                   /* int productID=  productList.get(posforsub).getVendorProduct().getId();
                        for(int j=0;j<=mProductModel.getSuccess().size();j++){
                            if(productID==mProductModel.getSuccess().get(j).getVendorProduct().getId()){
                                //   Log.e("ival" + j, "%%%%%%%%%%");
                                AddToJsonProductList(val, j);

                                return;

                            }
                    }*/
                }


            }
        });

        return view;

    }
    /*public void update(List<LocationListNameModel> rowItems) {
        city=rowItems;
    }*/
    private static class CityItemHolder {
        TextView city,mValue,price_value,itemDescription;
        EditText editmValue;
        ImageView mSubImg,mAddImg;
        ImageView mItemImg;
        String logo;
   }
}