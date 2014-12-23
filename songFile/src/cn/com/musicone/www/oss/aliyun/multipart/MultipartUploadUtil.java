/**
 * 
 */
package cn.com.musicone.www.oss.aliyun.multipart;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.oss.aliyun.OSSBucketException;
import cn.com.musicone.www.oss.aliyun.OSSKeyException;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.ListMultipartUploadsRequest;
import com.aliyun.oss.model.ListPartsRequest;
import com.aliyun.oss.model.MultipartUpload;
import com.aliyun.oss.model.MultipartUploadListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.PartListing;
import com.aliyun.oss.model.PartSummary;

/**
 * @author Administrator
 *
 */
public class MultipartUploadUtil {
	protected static final Logger logger = LogManager
			.getLogger(MultipartUploadUtil.class);

	/**
	 * 是否存在分块上传事件,存在返回此事件的uploadId
	 * 
	 * @param client
	 * @param bucketName
	 * @param key
	 * @return
	 * @throws OSSBucketException
	 * @throws OSSKeyException
	 */
	public static String isExistMultipartUpload(OSS client, String bucketName,
			String key) throws OSSException, ClientException {
		ListMultipartUploadsRequest request = new ListMultipartUploadsRequest(
				bucketName);
		MultipartUploadListing lists = client.listMultipartUploads(request);
		if (lists == null) {
			return null;
		}
		List<MultipartUpload> parts = lists.getMultipartUploads();
		if (parts == null) {
			return null;
		}
		for (MultipartUpload multipart : parts) {
			if (multipart == null) {
				continue;
			}
			if (multipart.getKey().equals(key)) {
				return multipart.getUploadId();
			}
		}
		return null;
	}

	/**
	 * 创建或直接获取已有上传事件ID
	 * @param client
	 * @param bucketName
	 * @param key
	 * @param objectMetadata 文件的属性说明
	 * @param isEachExistMultipartUpload 是否采用断点续传
	 * @return
	 * @throws OSSBucketException
	 * @throws OSSKeyException
	 * @throws OSSException
	 * @throws ClientException
	 */
	public static String createOrGetMultipartUploadRequest(OSS client,
			String bucketName, String key,ObjectMetadata objectMetadata, boolean isEachExistMultipartUpload) throws OSSException, ClientException {
		String uploadId = null;
		if(isEachExistMultipartUpload){
			uploadId = isExistMultipartUpload(client, bucketName, key);
			if (StringUtils.isNotBlank(uploadId)) {
				return uploadId;
			}
		}
		InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key);
		if(objectMetadata != null){
			request.setObjectMetadata(objectMetadata);
		}
		InitiateMultipartUploadResult result = client.initiateMultipartUpload(request);
		uploadId = result.getUploadId();
		return uploadId;
	}
	
	// 完成一个multi-part请求。
    public static void completeMultipartUpload(OSS client,String bucketName, String key, String uploadId, List<PartETag> eTags)throws OSSException, ClientException {
        //为part按partnumber排序
        Collections.sort(eTags, new Comparator<PartETag>(){
            public int compare(PartETag arg0, PartETag arg1) {
                PartETag part1= arg0;
                PartETag part2= arg1;
                return part1.getPartNumber() - part2.getPartNumber();
            }  
        });
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, key, uploadId, eTags);
        client.completeMultipartUpload(completeMultipartUploadRequest);
    }
    
    public static List<PartSummary> getPartList(OSS client,String bucketName, String key, String uploadId){
    	ListPartsRequest request = new ListPartsRequest(bucketName, key, uploadId);
    	PartListing part = client.listParts(request);
    	if(part == null){
    		return null;
    	}
    	return part.getParts();
    }

}
