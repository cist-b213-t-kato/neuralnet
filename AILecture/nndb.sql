DROP TABLE weight;

CREATE TABLE weight (
	type TEXT(32),
	srcidx INTEGER,
	dstidx INTEGER,
	value REAL,
	PRIMARY KEY( type, srcidx, dstidx )
);

REPlACE INTO weight VALUES( 'w', 0, 0, 1.13 );
REPLACE INTO weight VALUES( 'v', 0, 0, 5.14 );

SELECT * FROM weight;





DROP TABLE data;

CREATE TABLE data (
--	size INTEGER,
	image TEXT(256) PRIMARY KEY,
	teach TEXT(32)
);

REPLACE INTO data VALUES( '0,1,1,0', '0,0,0,0,1,0,0,0,0,0' );

SELECT * FROM data;














