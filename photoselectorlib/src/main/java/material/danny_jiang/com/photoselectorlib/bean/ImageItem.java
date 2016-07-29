package material.danny_jiang.com.photoselectorlib.bean;

import java.io.Serializable;

/**
 * 一个图片对象
 * @author Administrator
 */
public class ImageItem implements Serializable {
	private static final long serialVersionUID = -3917764214596708310L;
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public boolean isSelected = false;
}
