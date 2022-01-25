.PHONY: init-sqlite
init-sqlite:
	@mkdir -p sqlite
	@sqlite3 sqlite/tgbot.db "create table word_spec ( id INTEGER PRIMARY KEY, word varchar(255) not null, create_at datetime not null, american_pronunciation varchar(255) not null, british_pronunciation varchar(255) not null, google_pronunciation varchar(255), img_list text, video_list text, explanation_1 text, explanation_2 text );"

.PHONY: docker-run
docker-run:
	@docker build -t tgbot .
	@docker run --name=tgbot01 -p 8081:8081 -v sqlite:/app/sqlite tgbot

.PHONY: all
all:
	@make init-sqlite
	@make docker-all
