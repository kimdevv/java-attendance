package attendance.dto;

import attendance.model.ExpulsionStatus;

public record CheckExpulsionResultDto(String nickname, long absentCount, long lateCount, long allAbsents, ExpulsionStatus expulsionStatus) {}
