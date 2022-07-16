package com.example.locasandroidtest.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.locasandroidtest.R;
import com.example.locasandroidtest.constant.Constants;
import com.example.locasandroidtest.model.ItemObject;
import com.example.locasandroidtest.viewModel.BaseViewModel;
import com.example.locasandroidtest.viewModel.MainViewModel;

import java.util.Locale;

public class ItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private static int ITEM_TYPE_IMAGE = 101;
    private static int ITEM_TYPE_SINGLE_CHOICE = 102;
    private static int ITEM_TYPE_COMMENT = 103;
    public MainViewModel viewModel;
    private static onItemClickListener onItemClickListener;

    public ItemListAdapter(Context context, MainViewModel mViewModel, onItemClickListener onItemClickListener) {
        this.context = context;
        this.viewModel = mViewModel;
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_IMAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_imagetype, parent, false);
            return new ImageTypeViewHolder(view);
        } else if (viewType == ITEM_TYPE_SINGLE_CHOICE) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_singlechoice, parent, false);
            return new SigleChoiceTypeViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_commenttype, parent, false);
            return new CommetTypeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemObject itemObject = viewModel.getLiveData().getValue().get(position);
        if (holder.getItemViewType() == ITEM_TYPE_IMAGE) {
            ImageTypeViewHolder imageTypeViewHolder = (ImageTypeViewHolder) holder;
            imageTypeViewHolder.tvImageHeader.setText(itemObject.getTitle());
            if (itemObject.getCapturedImageFile() != null) {
                Glide.with(context).load(itemObject.getCapturedImageFile()).into(imageTypeViewHolder.imageView);
                imageTypeViewHolder.ivDelete.setVisibility(View.VISIBLE);
                imageTypeViewHolder.addPhotoText.setVisibility(View.GONE);
            } else {
                imageTypeViewHolder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
                imageTypeViewHolder.ivDelete.setVisibility(View.GONE);
                imageTypeViewHolder.addPhotoText.setVisibility(View.VISIBLE);
            }
            imageTypeViewHolder.imageView.setOnClickListener(v -> {
                if (itemObject.getCapturedImageFile() == null)
                    onItemClickListener.onImageCaptureClicked(position, imageTypeViewHolder, itemObject);
                else
                    onItemClickListener.onViewImageClicked(itemObject);
            });
            imageTypeViewHolder.ivDelete.setOnClickListener(v -> {
                itemObject.setCapturedImageFile(null);
                imageTypeViewHolder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
                imageTypeViewHolder.ivDelete.setVisibility(View.GONE);
                notifyItemChanged(position);
            });
        } else if (holder.getItemViewType() == ITEM_TYPE_SINGLE_CHOICE) {
            SigleChoiceTypeViewHolder sigleChoiceTypeViewHolder = (SigleChoiceTypeViewHolder) holder;
            sigleChoiceTypeViewHolder.setItemObject(itemObject);
            sigleChoiceTypeViewHolder.tvTitle.setText(itemObject.getTitle());
            sigleChoiceTypeViewHolder.radioButton1.setText(itemObject.getDataMap().getOptions().get(0));
            sigleChoiceTypeViewHolder.radioButton2.setText(itemObject.getDataMap().getOptions().get(1));
            if (itemObject.getDataMap().getOptions().size() > 2) {
                sigleChoiceTypeViewHolder.radioButton3.setText(itemObject.getDataMap().getOptions().get(2));
                sigleChoiceTypeViewHolder.radioButton3.setVisibility(View.VISIBLE);
            } else
                sigleChoiceTypeViewHolder.radioButton3.setVisibility(View.GONE);
            sigleChoiceTypeViewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // This will get the radiobutton that has changed in its check state
                    RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                    // This puts the value (true/false) into the variable
                    if (checkedRadioButton!=null) {
                        boolean isChecked = checkedRadioButton.isChecked();
                        // If the radiobutton that has changed in check state is now checked...
                        if (isChecked) {
                            // Changes the textview's text to "Checked: example radiobutton text"
                            itemObject.setCheckedButtonId(checkedId);
                        }
                    }
                }
            });

            if (itemObject.getCheckedButtonId() != -1) {
                sigleChoiceTypeViewHolder.radioGroup.check(itemObject.getCheckedButtonId());
            }

        } else {
            CommetTypeViewHolder commetTypeViewHolder = (CommetTypeViewHolder) holder;
            commetTypeViewHolder.setItemObject(itemObject);
            commetTypeViewHolder.etComment.setText(itemObject.getComment());
            commetTypeViewHolder.toggleButton.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    commetTypeViewHolder.etComment.setVisibility(View.VISIBLE);
                    itemObject.setToggled(b);
                } else {
                    commetTypeViewHolder.etComment.setVisibility(View.GONE);
                    itemObject.setToggled(b);
                }
            });
            if (itemObject.isToggled()) {
                commetTypeViewHolder.toggleButton.setChecked(itemObject.isToggled());
                commetTypeViewHolder.etComment.setVisibility(View.VISIBLE);
            } else {
                commetTypeViewHolder.toggleButton.setChecked(false);
                commetTypeViewHolder.etComment.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (viewModel.getLiveData().getValue() != null)
            return viewModel.getLiveData().getValue().size();
        else
            return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (viewModel.getLiveData().getValue().get(position).getType().equals(Constants.TYPE_PHOTO))
            return ITEM_TYPE_IMAGE;
        else if (viewModel.getLiveData().getValue().get(position).getType().equals(Constants.TYPE_SINGLE_CHOICE))
            return ITEM_TYPE_SINGLE_CHOICE;
        else
            return ITEM_TYPE_COMMENT;
    }

    public void updateData() {
        notifyDataSetChanged();
    }

    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {
        TextView tvImageHeader,addPhotoText;
        ImageView imageView, ivDelete;

        public ImageTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvImageHeader = itemView.findViewById(R.id.tvImageHeader);
            addPhotoText = itemView.findViewById(R.id.addPhotoText);
            imageView = itemView.findViewById(R.id.imageView);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }

    public class SigleChoiceTypeViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        RadioGroup radioGroup;
        RadioButton radioButton1, radioButton2, radioButton3;
        ItemObject itemObject;

        public void setItemObject(ItemObject itemObject) {
            this.itemObject = itemObject;
        }

        public SigleChoiceTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            radioButton1 = itemView.findViewById(R.id.radioButton1);
            radioButton2 = itemView.findViewById(R.id.radioButton2);
            radioButton3 = itemView.findViewById(R.id.radioButton3);
        }
    }

    public class CommetTypeViewHolder extends RecyclerView.ViewHolder {
        ToggleButton toggleButton;
        EditText etComment;
        MyTextWatcher myTextWatcher;
        ItemObject itemObject;

        private void setItemObject(ItemObject itemObject) {
            this.itemObject = itemObject;
        }

        public CommetTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            toggleButton = itemView.findViewById(R.id.toggleButton);
            etComment = itemView.findViewById(R.id.etComment);
            myTextWatcher = new MyTextWatcher();
            myTextWatcher.updatePostion(getAdapterPosition());
            etComment.addTextChangedListener(myTextWatcher);
        }

        class MyTextWatcher implements TextWatcher {
            int position;

            public void updatePostion(int position) {
                this.position = position;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                itemObject.setComment(editable.toString().trim());
            }
        }
    }

    public interface onItemClickListener {
        void onImageCaptureClicked(int position, ImageTypeViewHolder holder, ItemObject itemObject);

        void onViewImageClicked(ItemObject itemObject);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }
}
