package com.example.furniture.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniture.R;
import com.example.furniture.adapters.CategoryAdapter;
import com.example.furniture.adapters.ProductAdapter;
import com.example.furniture.models.Cart;
import com.example.furniture.models.Category;

import com.example.furniture.models.Favourite;
import com.example.furniture.models.OrderDetail;
import com.example.furniture.models.Product;
import com.example.furniture.models.User;
import com.example.furniture.services.Api;
import com.example.furniture.services.DownloadDataCategory;
import com.example.furniture.services.DownloadDataProduct;
import com.example.furniture.services.DownloadDataProductByCategory;
import com.example.furniture.services.OnDataCategoryListener;
import com.example.furniture.services.OnDataProductListener;
import com.example.furniture.utilities.OnDataPassProduct;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputLayout;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements OnDataCategoryListener, OnDataProductListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    private User user;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private static final String urlProduct = Api.urlLocal + "product";

    private RecyclerView recyclerViewCategory, recyclerViewProduct;

    private ShimmerFrameLayout shimmerCategory, shimmerProduct;

    private OnDataPassProduct dataPassProduct;

    private LinearLayout linearLayout;

    private TextInputLayout editSearch;

    public HomeFragment(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewCategory = view.findViewById(R.id.recycleViewCategory);
        recyclerViewProduct = view.findViewById(R.id.recycleViewProduct);

        shimmerCategory = view.findViewById(R.id.shimmerCategory);
        shimmerProduct = view.findViewById(R.id.shimmerProduct);

        editSearch = view.findViewById(R.id.editSearch);

        linearLayout = view.findViewById(R.id.layoutProduct);

        downloadCategory(view);

        return view;
    }

    private void downloadCategory(View view) {
        //run animate
        recyclerViewCategory.setVisibility(View.GONE);
        editSearch.setVisibility(View.GONE);
        shimmerCategory.setVisibility(View.VISIBLE);
        shimmerCategory.startShimmer();
        DownloadDataCategory downloadDataCategory = new DownloadDataCategory(view,
                this, recyclerViewCategory);
        downloadDataCategory.execute();
    }


    @Override
    public void onCompleteDataCategory(View view, ArrayList<Category> arrayList) {

        CategoryAdapter categoryAdapter = new CategoryAdapter(view.getContext(), arrayList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setAdapter(categoryAdapter);

        categoryAdapter.setSetOnClickItem(new CategoryAdapter.SetOnClickItemCategory() {
            @Override
            public void setOnClickItemCategory(View view, int pos) {
                downloadProduct(view, arrayList.get(pos).getId());
            }
        });

        downloadProduct(view, arrayList.get(0).getId());

        //stop animate
        shimmerCategory.stopShimmer();
        shimmerCategory.setVisibility(View.GONE);
        recyclerViewCategory.setVisibility(View.VISIBLE);
        editSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void onErrorDataCategory(View view, String error) {
        Toast.makeText(view.getContext(), error, Toast.LENGTH_SHORT).show();
    }


    private void downloadProduct(View view, String id) {
        linearLayout.setVisibility(View.GONE);
        shimmerProduct.setVisibility(View.VISIBLE);
        shimmerProduct.startShimmer();
        DownloadDataProductByCategory downloadDataProductByCategory = new DownloadDataProductByCategory(view.getContext(),
                this, id);
        downloadDataProductByCategory.execute();
    }

    @Override
    public void onCompleteDataProduct(Context view, ArrayList<Product> arrayList) {
        ProductAdapter productAdapter = new ProductAdapter(view, arrayList);
        GridLayoutManager layoutManager = new GridLayoutManager(view,
                2);
        recyclerViewProduct.setLayoutManager(layoutManager);
        recyclerViewProduct.setAdapter(productAdapter);

        productAdapter.setSetOnClickItemPurchase(new ProductAdapter.SetOnClickItemPurchase() {
            @Override
            public void setOnClickItemPurchase(View view, int pos) {
            }
        });


        productAdapter.setOnClickItemProduct(new ProductAdapter.SetOnClickItemProduct() {
            @Override
            public void setOnClickItemProduct(View view, int pos) {
                Product p = arrayList.get(pos);
                sendDataToActivity(user,p,"Home");
            }
        });

        shimmerProduct.stopShimmer();
        shimmerProduct.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);

        editSearch.getEditText().setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        || actionId == EditorInfo.IME_ACTION_DONE) {

                    InputMethodManager imm = (InputMethodManager) view.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editSearch.getApplicationWindowToken(), 0);


                    arrayList.clear();
                    productAdapter.notifyDataSetChanged();

                    shimmerProduct.startShimmer();
                    shimmerProduct.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);

                    DownloadDataProduct downloadDataProduct = new DownloadDataProduct(view, new OnDataProductListener() {
                        @Override
                        public void onCompleteDataProduct(Context view, ArrayList<Product> arrayList) {


                            String text = editSearch.getEditText().getText().toString();

                            ArrayList<Product> newProduct = new ArrayList<>();

                            if (!text.isEmpty()) {
                                for (int i = 0; i < arrayList.size(); i++) {
                                    if (arrayList.get(i).getName().toLowerCase().contains(text)
                                            || arrayList.get(i).getPrice().contains(text)) {
                                        newProduct.add(arrayList.get(i));
                                    }
                                }
                                if (newProduct.size() == 0) {
                                    productAdapter.setProductArrayList(arrayList);
                                    productAdapter.setOnClickItemProduct(new ProductAdapter.SetOnClickItemProduct() {
                                        @Override
                                        public void setOnClickItemProduct(View view, int pos) {
                                            Product p = arrayList.get(pos);
                                            sendDataToActivity(user,p,"Home");
                                        }
                                    });
                                }
                                productAdapter.setProductArrayList(newProduct);
                                productAdapter.setOnClickItemProduct(new ProductAdapter.SetOnClickItemProduct() {
                                    @Override
                                    public void setOnClickItemProduct(View view, int pos) {
                                        Product p = newProduct.get(pos);
                                        sendDataToActivity(user,p,"Home");
                                    }
                                });
                            } else {
                                productAdapter.setProductArrayList(arrayList);
                                productAdapter.setOnClickItemProduct(new ProductAdapter.SetOnClickItemProduct() {
                                    @Override
                                    public void setOnClickItemProduct(View view, int pos) {
                                        Product p = arrayList.get(pos);
                                        sendDataToActivity(user,p,"Home");
                                    }
                                });
                            }

                            productAdapter.notifyDataSetChanged();


                            shimmerProduct.stopShimmer();
                            shimmerProduct.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }


                        @Override
                        public void onErrorDataProduct(Context view, String error) {

                        }

                        @Override
                        public void onCompleteDataFavProduct(Context view, ArrayList<Product> products, ArrayList<Favourite> favourites) {

                        }

                        @Override
                        public void onCompleteDataCartProduct(Context view, ArrayList<Product> products, ArrayList<Cart> carts) {

                        }

                        @Override
                        public void onCompleteDataOrderDetailProduct(Context view, ArrayList<Product> products, ArrayList<OrderDetail> carts) {

                        }
                    });

                    downloadDataProduct.execute();


                    return true;
                }
                return false;
            }
        });


    }


    @Override
    public void onErrorDataProduct(Context view, String error) {
        Toast.makeText(view, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleteDataFavProduct(Context view, ArrayList<Product> products, ArrayList<Favourite> favourites) {

    }

    @Override
    public void onCompleteDataCartProduct(Context view, ArrayList<Product> products, ArrayList<Cart> carts) {

    }

    @Override
    public void onCompleteDataOrderDetailProduct(Context view, ArrayList<Product> products, ArrayList<OrderDetail> carts) {

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPassProduct = (OnDataPassProduct) context;
    }

    public void sendDataToActivity(User user,Product product,String tag) {
        dataPassProduct.onDataPassProduct(user,product,tag);
    }


}