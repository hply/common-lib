package cn.iyuxuan.library.imagepicker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.iyuxuan.library.R;
import cn.iyuxuan.library.imagepicker.bean.ImageFolder;

public class ImageFolderAdapter extends BaseAdapter {

    private ImagePicker imagePicker;
    private Activity mActivity;
    private LayoutInflater mInflater;
    private int mImageSize;
    private List<ImageFolder> imageFolders;
    private int lastSelected = 0;

    public ImageFolderAdapter(Activity activity, List<ImageFolder> folders) {
        mActivity = activity;
        if (folders != null && folders.size() > 0) {
            imageFolders = folders;
        } else {
            imageFolders = new ArrayList<>();
        }

        imagePicker = ImagePicker.getInstance();
        mImageSize = Utils.getImageItemWidth(mActivity);
        mInflater = LayoutInflater.from(activity);
    }

    public void refreshData(List<ImageFolder> folders) {
        if (folders != null && folders.size() > 0) {
            imageFolders = folders;
        } else {
            imageFolders.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageFolders == null ? 0 : imageFolders.size();
    }

    @Override
    public ImageFolder getItem(int position) {
        return imageFolders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_folder_list_item, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageFolder folder = getItem(position);
        holder.folderName.setText(folder.name);
        holder.imageCount.setText(mActivity.getString(R.string.folder_image_count, folder.images.size()));
        imagePicker.getImageLoader().displayImage(mActivity, folder.cover.path, holder.cover, mImageSize, mImageSize);

        if (lastSelected == position) {
//            holder.folderCheck.setVisibility(View.VISIBLE);
            holder.folderCheck.setImageResource(R.mipmap.list_selected);
        } else {
//            holder.folderCheck.setVisibility(View.INVISIBLE);
            holder.folderCheck.setImageResource(R.mipmap.list_unselected);
        }

        return convertView;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) {
            return;
        }
        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    private class ViewHolder {
        ImageView cover;
        TextView folderName;
        TextView imageCount;
        ImageView folderCheck;

        public ViewHolder(View view) {
            cover = (ImageView) view.findViewById(R.id.iv_cover);
            folderName = (TextView) view.findViewById(R.id.tv_folder_name);
            imageCount = (TextView) view.findViewById(R.id.tv_image_count);
            folderCheck = (ImageView) view.findViewById(R.id.iv_folder_check);
            view.setTag(this);
        }
    }
}