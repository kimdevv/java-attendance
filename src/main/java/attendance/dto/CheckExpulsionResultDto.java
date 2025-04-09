package attendance.dto;

import attendance.model.ExpulsionStatus;

public record CheckExpulsionResultDto(String nickname, long absentCount, long lateCount, long totalAbsents, ExpulsionStatus expulsionStatus) {}
