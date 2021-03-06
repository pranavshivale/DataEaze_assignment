
val df1=spark.read.format("csv").option("header",true).load("s3://ngap2-user-data/dsmimengineeringnonprod/PSadan/startup.csv")
val df2=spark.read.format("parquet").option("header",true).load("s3://ngap2-user-data/dsmimengineeringnonprod/PSadan/consumerInternet.parquet")
val finalDf=df1.union(df2)
finalDf.createOrReplaceTempView("startup_data")
val q1_op=spark.sql("select max(City) as City,count(*) from Pune_startup where City='Pune'")
q1_op.show()
q1_op.write.format("csv").option("header", "true").save("s3://ngap2-user-data/dsmimengineeringnonprod/PSadan/tmp/output/q1")
val q2_op=spark.sql("select City,InvestmentnType,count(*) from startup_data where City='Pune' group by City,InvestmentnType having InvestmentnType = 'Seed/ Angel Funding'")
q2_op.show()
q2_op.write.format("csv").option("header", "true").save("s3://ngap2-user-data/dsmimengineeringnonprod/PSadan/tmp/output/q2")
val new_4 = finalDf.withColumn("Amount_in_USD", when(col("Amount_in_USD").startsWith("\\"), "0").when(col("Amount_in_USD").endsWith("+"), "0").otherwise(col("Amount_in_USD")))
val abcd=new_4.withColumn("Amount_in_USD", when($"Amount_in_USD".endsWith("N/A"),regexp_replace($"Amount_in_USD","N/A","0")).when($"Amount_in_USD"endsWith("nan"),regexp_replace($"Amount_in_USD","nan","0")).when($"Amount_in_USD"endsWith("Private Equity"),regexp_replace($"Amount_in_USD","Private Equity","0")).when($"Amount_in_USD"endsWith("Undisclosed"),regexp_replace($"Amount_in_USD","Undisclosed","0")).when($"Amount_in_USD"endsWith("undisclosed"),regexp_replace($"Amount_in_USD","undisclosed","0")).when($"Amount_in_USD"endsWith("unknown"),regexp_replace($"Amount_in_USD","unknown","0")).otherwise(col("Amount_in_USD")))
val p = abcd.withColumn("Amount_in_USD", regexp_replace($"Amount_in_USD", "\\,", ""))
val pranav=p.withColumn("Amount_in_USD",col("Amount_in_USD").cast("Integer"))
pranav.createOrReplaceTempView("Q3")
val q3_op=spark.sql("select sum(Amount_in_USD) as Total_amount from Q3 where City='Pune'")
q3_op.show()
q3_op.write.format("csv").option("header", "true").save("s3://ngap2-user-data/dsmimengineeringnonprod/PSadan/tmp/output/q3")
val q4_op=spark.sql("select Industry_Vertical,count(Startup_Name) as cnt from startup_data group by Industry_vertical order by cnt desc limit 5")
q4_op.show()
q4_op.write.format("csv").option("header", "true").save("s3://ngap2-user-data/dsmimengineeringnonprod/PSadan/tmp/output/q4")
val df3=pranav.withColumn("year", col("Date").substr(-4, 4))
df3.createOrReplaceTempView("q5table")
val x=spark.sql("select year,Investors_name,sum(Amount_in_USD) as amount from q5table group by year,Investors_name")
x.createOrReplaceTempView("q5_max_table")
val q5=spark.sql("select year, max(Investors_name) as Investors_name,max(amount) as amount from q5_max_table group by year order by year")
q5.createOrReplaceTempView("q5_table")
val q5_op=spark.sql("select * from q5_table where year not in ('/015') ")
q5_op.show()
q5_op.write.format("csv").option("header", "true").save("s3://ngap2-user-data/dsmimengineeringnonprod/PSadan/tmp/output/q5")
df3.createOrReplaceTempView("q6table")
val x2=spark.sql("select City,Startup_Name,sum(Amount_in_USD) as amount from q6table group by City,Startup_Name")
x2.createOrReplaceTempView("q6_max_table")
val q6_op=spark.sql("select City, max(Startup_name) as Startup_Name,max(amount) as amount from q6_max_table group by City")
q6_op.show()
q6_op.write.format("csv").option("header", "true").save("s3://ngap2-user-data/dsmimengineeringnonprod/PSadan/tmp/output/q6")


