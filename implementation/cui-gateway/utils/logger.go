package utils

import (
	"time"

	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
)

var Logger *zap.Logger
var LoggerSugar *zap.SugaredLogger

func InitializeLoggers() {
	// Logger, _ = zap.NewProduction()

	loggerConfig := zap.NewProductionConfig()
	loggerConfig.EncoderConfig.TimeKey = "timestamp"
	loggerConfig.EncoderConfig.EncodeTime = zapcore.TimeEncoderOfLayout(time.RFC3339)

	Logger, _ = loggerConfig.Build()
	LoggerSugar = Logger.Sugar()
}
