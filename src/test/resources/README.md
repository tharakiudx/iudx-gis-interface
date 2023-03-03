### Making configurable base path
- Base path can be added in postman environment file or in postman.
- `IUDX_GIS_Server_APIs_V4.0.environment.json` has **values** array which has fields named **dxApiBasePath** whose **value** is currently set to `ngsi-ld/v1`, **adminBasePath** with **value** `admin/gis`, **dxAuthBasePath** with value `auth/v1`.
- These **values** could be changed according to the deployment and then the collection with the environment file can be uploaded to Postman
- For the changing the **dxApiBasePath**, **adminBasePath**,**adminBasePath** values in postman after importing the collection and environment files, locate `GIS Environment` from **Environments** in sidebar of Postman application.
- To know more about Postman environments, refer : [postman environments](https://learning.postman.com/docs/sending-requests/managing-environments/)
- The **CURRENT VALUE** of the variable could be changed


