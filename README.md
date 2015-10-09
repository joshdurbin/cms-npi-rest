# cms-npi-rest
Centers for Medicare and Medicaid Services (CMS) National Provider Identifier (NPI) REST service built atop Ratpack, MxMongo hosted at Heroku.

Data load procedures:

1. Read the [Data Dissemination](https://www.cms.gov/Regulations-and-Guidance/HIPAA-Administrative-Simplification/NationalProvIdentStand/DataDissemination.html) page for the National Provider Identifier Standard (NPI). Specifically, it's good to read the [Readme](https://www.cms.gov/Regulations-and-Guidance/HIPAA-Administrative-Simplification/NationalProvIdentStand/Downloads/Data_Dissemination_File-Readme.pdf) know and unerstand the [code values](https://www.cms.gov/Regulations-and-Guidance/HIPAA-Administrative-Simplification/NationalProvIdentStand/Downloads/Data_Dissemination_File-Code_Values.pdf).
2. Follow the link the [NPI Downloadable File](http://download.cms.gov/nppes/NPI_Files.html).
3. Download the single zip file under the "Full Replacement Monthly NPI File" heading (should be half a gigabyte or so).
4. Unpack the file
5. With the latest Java 8 JDK and [Groovy](http://www.groovy-lang.org/) installed (recommend via [sdkman](http://sdkman.io/)), execute the [CSV conversion script](https://github.com/joshdurbin/cms-npi-rest/blob/data_cleaning_scripts/FormatNPIData.groovy) found in the [data-cleaning-scripts](https://github.com/joshdurbin/cms-npi-rest/tree/data_cleaning_scripts) branch.
4. Place the script in the same directory as the csv file. Modify the script on line 24 and execute. I intentionally didn't want the ENTIRE dataset for this project, so I calculate the weights for each state within the US and use those values to randomly pick individuals and organizations for output. The script is straight forward -- modification to output everything is simple enough. The script will produce a JSON file, `output.json`.
5. Import script output into Mongo

  * `mongoimport -v --host=127.0.0.1 --port=27017 --db cms-npi-rest --collection npi_records output.json`

7. Establish indexes on collection

  ```javascript
  db.npi_records.createIndex(
    {
      npiCode: 1,
      type: 1,
    },
    {
      name: "NonTextIndex"
    }
)
```

8. Establish text indexes on collection

  ```javascript
  db.npi_records.createIndex(
    {
      firstName: "text",
      middleName: "text",
      lastName: "text",
      name: "text",
      otherName: "text",
    },
    {
      name: "TextIndex"
    }
)
```

[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy?template=https://github.com/joshdurbin/cms-npi-rest)  
