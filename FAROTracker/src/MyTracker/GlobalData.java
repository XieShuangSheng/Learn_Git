/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

/**
 *
 * @author szhc
 */
enum CURRENTPROCESS {
    NONE_PROCESS,//无
    CHECK_PAGE_SINGLEREAD_PROCESS,//校准手动
    CHECK_PAGE_CONTINUE_PROCESS,//校准自动
    POSE_PAGE_SINGLEREAD_PROCESS,//位姿手动
    POSE_PAGE_CONTINUE_PROCESS,//位姿自动
    TRAJECTORY_PAGE_SINGLEREAD_PROCESS,//轨迹手动
    TRAJECTORY_PAGE_CONTINUE_PROCESS,//轨迹自动
    TRAJECTORY_PAGE_STOPMEASURE_PROCESS,//停止轨迹检测
    COORDTRANS_SINGLEREAD_PROCESS,//
    
    SWINGERROR_PAGE_SINGLEREAD_PROCESS,//摆动偏差手动
    SWINGERROR_PAGE_CONTINUE_PROCESS,//摆动偏差自动
    SWINGERROR_PAGE_BREAK_AUTO_PROCESS,//终止摆动偏差检测
    
    TURN_MANUAL_PROCESS,
    POSE_PAGE_BREAK_AUTO_PROCESS,   //终止位姿检测
    DISCONNECT_PROCESS,
}

enum TYPESEL {
    SIXDOF_ROBOT,
    SCARA_ROBOT,
}
