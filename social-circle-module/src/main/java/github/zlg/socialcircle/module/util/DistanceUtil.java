package github.zlg.socialcircle.module.util;

import java.awt.geom.Point2D;

/**
 * @program: social-circle-main
 * @description: 将地球抽象为球体，计算两点之间的距离
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-09 13:49
 **/
public class DistanceUtil {

    private static final double EARTH_RADIUS = 6371393; // 平均半径,单位：m

    /**
     * 通过AB点经纬度获取距离
     * 使用高德 api 会有次数限制，本算法没有考虑海拔影响，对于特殊地势，会有较大误差
     * @return 距离(单位：米)
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
        double radiansAX = Math.toRadians(longitude1); // A经弧度
        double radiansAY = Math.toRadians(latitude1); // A纬弧度
        double radiansBX = Math.toRadians(longitude2); // B经弧度
        double radiansBY = Math.toRadians(latitude2); // B纬弧度

        // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
                + Math.sin(radiansAY) * Math.sin(radiansBY);
        double acos = Math.acos(cos); // 反余弦值
        return EARTH_RADIUS * acos; // 最终结果
    }
}
