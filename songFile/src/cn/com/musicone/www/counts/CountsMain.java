/**
 * 
 */
package cn.com.musicone.www.counts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.counts.model.CountModel;
import cn.com.musicone.www.counts.model.SearchReqLogModel;
import cn.com.musicone.www.counts.service.CountQueryService;
import cn.com.musicone.www.counts.service.impl.CountQuerySerivceImpl;
import cn.com.musicone.www.mybatis.MybatisUtil;

/**
 * @author Administrator
 *
 */
public class CountsMain {

	protected static final Logger logger = LogManager
			.getLogger(CountsMain.class);

	public static void main(String[] args) throws IOException {
		MybatisUtil.init();
		timerStart();
	}

	public static void timerStart() {
		Date current = new Date();
		long currentTimes = current.getTime();
		long beforeTimes = currentTimes
				+ CountsGetIdUtil.MEMBER_LOGIN_EXPIRATION_TIMES_DAY;
		current = new Date(beforeTimes);
		String str = CountsGetIdUtil.getFormatDate(current);
		str = str + CountsGetIdUtil.START_DATE_STR;
		current = CountsGetIdUtil.getDateFromStr(str, "yyyy-MM-dd hh:mm:ss");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				start();
			}
		}, current, CountsGetIdUtil.MEMBER_LOGIN_EXPIRATION_TIMES_DAY);
	}

	public static CountQueryService countQueryService = new CountQuerySerivceImpl();

	public static void start() {
		Date current = new Date();
		long currentTimes = current.getTime();
		long beforeTimes = currentTimes
				- CountsGetIdUtil.MEMBER_LOGIN_EXPIRATION_TIMES_DAY;
		Date date = new Date(beforeTimes);
		countSingers(date);
		countAlbums(date);
		countSongs(date);
		countAlbumsCollect(date);
	}

	/**
	 * 按每天统计歌手的有效点击量，并记录到指定的日志表里面
	 * 
	 * @param date
	 */
	public static void countSingers(Date date) {
		List<CountModel> datas = null;
		SearchReqLogModel qbo = new SearchReqLogModel();
		qbo.setStartDate(CountsGetIdUtil.getStartDate(date));
		qbo.setEndDate(CountsGetIdUtil.getEndDate(date));
		qbo.setReqPathLike("/api/v3/enjoy/singers%/songs");
		qbo.setFilterReqParams(true);
		qbo.setPageSize(1000);
		int index = 0;
		while (true) {
			try {
				qbo.setPageStart(index);
				datas = countQueryService.querySingerCountsByDate(qbo);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				continue;
			}
			if (datas == null || datas.isEmpty()) {
				break;
			}
			List<CountModel> saveDatas = new ArrayList<CountModel>();
			for (CountModel countModel : datas) {
				if (countModel == null) {
					continue;
				}
				int reqId = CountsGetIdUtil.getId(countModel.getReqPath(),
						CountsGetIdUtil.REGEX_SINGER_PATH);
				if (reqId <= 0) {
					continue;
				}
				countModel.setReqId(reqId);
				countModel.setLogTime(date);
				saveDatas.add(countModel);
			}
			try {
				countQueryService.addSingerCountLog(saveDatas);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			index += qbo.getPageSize();
		}
	}

	/**
	 * 按每天统计专辑的有效点击量，并记录到指定的日志表里面
	 * 
	 * @param date
	 */
	public static void countAlbums(Date date) {
		List<CountModel> datas = null;
		SearchReqLogModel qbo = new SearchReqLogModel();
		qbo.setStartDate(CountsGetIdUtil.getStartDate(date));
		qbo.setEndDate(CountsGetIdUtil.getEndDate(date));
		qbo.setReqPathLike("/api/v3/enjoy/albums%/songs");
		qbo.setFilterReqParams(true);
		qbo.setPageSize(1000);
		int index = 0;
		while (true) {
			try {
				qbo.setPageStart(index);
				datas = countQueryService.querySingerCountsByDate(qbo);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				continue;
			}
			if (datas == null || datas.isEmpty()) {
				break;
			}
			List<CountModel> saveDatas = new ArrayList<CountModel>();
			for (CountModel countModel : datas) {
				if (countModel == null) {
					continue;
				}
				int reqId = CountsGetIdUtil.getId(countModel.getReqPath(),
						CountsGetIdUtil.REGEX_ALBUM_PATH);
				if (reqId <= 0) {
					continue;
				}
				countModel.setReqId(reqId);
				countModel.setLogTime(date);
				saveDatas.add(countModel);
			}
			try {
				countQueryService.addAlbumCountLog(saveDatas);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			index += qbo.getPageSize();
		}
	}

	/**
	 * 按每天统计歌曲的有效点击量，并记录到指定的日志表里面
	 * 
	 * @param date
	 */
	public static void countSongs(Date date) {
		List<CountModel> datas = null;
		SearchReqLogModel qbo = new SearchReqLogModel();
		qbo.setStartDate(CountsGetIdUtil.getStartDate(date));
		qbo.setEndDate(CountsGetIdUtil.getEndDate(date));
		qbo.setReqPathLike("/api/v3/play%");
		qbo.setFilterReqParams(false);
		qbo.setPageSize(1000);
		int index = 0;
		while (true) {
			try {
				qbo.setPageStart(index);
				datas = countQueryService.querySingerCountsByDate(qbo);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				continue;
			}
			if (datas == null || datas.isEmpty()) {
				break;
			}
			List<CountModel> saveDatas = new ArrayList<CountModel>();
			for (CountModel countModel : datas) {
				if (countModel == null) {
					continue;
				}
				int reqId = CountsGetIdUtil.getId(countModel.getReqPath(),
						CountsGetIdUtil.REGEX_ENJOYCD_PATH);
				if (reqId <= 0) {
					reqId = CountsGetIdUtil.getId(countModel.getReqPath(),
							CountsGetIdUtil.REGEX_CHERRYTIME_PATH);
				}
				if (reqId <= 0) {
					continue;
				}
				countModel.setReqId(reqId);
				countModel.setLogTime(date);
				saveDatas.add(countModel);
			}
			try {
				countQueryService.addSongCountLog(saveDatas);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			index += qbo.getPageSize();
		}
	}

	/**
	 * 按每天统计所有专辑的收藏量，并记录到指定的日志表里面
	 * 
	 * @param date
	 */
	public static void countAlbumsCollect(Date date) {
		SearchReqLogModel qbo = new SearchReqLogModel();
		qbo.setStartDate(CountsGetIdUtil.getStartDate(date));
		qbo.setEndDate(CountsGetIdUtil.getEndDate(date));
		try {
			CountModel datas = countQueryService.countAlbumCollect(qbo);
			if (datas == null) {
				return;
			}
			datas.setLogTime(date);
			countQueryService.addAlbumsCountLog(datas);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
