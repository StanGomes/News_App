# News App

News App powered by CBC cbc test api.
It uses new and experimental api's still in alpha stages. These should never be used in production apps under normal circumstances however I still wanted to try them out in this little project

The App uses MVVM pattern, with code clearly seperated in packages.</br>
It utilizes mappers to map network responses to domain data classes, and domain data to db entities and back to allow data transformations and easy testing.</br>
The database is the single source of truth, whenever the app fetches new data, it updates the database table. The data inside this table is then emitted to the UI.
All the business logic is seperated out from UI. </br>
Currently the repository is the actual data source that performs all network and db operations, but ideally it would be done in a data source object and the repository just gets the data and passes it to the ViewModel.
This way we can easily plug in any new data sources and not be tightly bound to just one data source (currently cbc api) </br>
News app should have pagination always for best ux however it was skipped here due to time constraints. </br>
Dependency Injection using Dagger Hilt
The ViewModel has been lightly unit tested with the repository mocked.

