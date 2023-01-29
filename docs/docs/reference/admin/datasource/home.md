!!! note

    Through the data source function, you can add support for various custom data sources, and perform subsequent data source operations, etc.

### Add data source

---

After entering the system, click the corresponding `DataSource` submenu under the top `Admin` menu to go to the function configuration function

![img.png](img.png)

Click the Add button on the far right of the content area (it's a + icon), After clicking, the Add Data Source window will pop up as follows

![img_1.png](img_1.png)

When we select a certain type of data source, the data source configuration information will be displayed in the top tab bar

![img_2.png](img_2.png)

Fill in the relevant configuration information according to the data source tab. After the information is entered, click the `Test` button at the bottom. After the test is successful, you can click the `Save` button to save.

![img_3.png](img_3.png)

After the data source is saved, the data source list will be automatically refreshed, roughly as follows

![img_4.png](img_4.png)

### Modify data source

---

Click the first button in `Action` in a data source in the list to modify the data source, the operation is similar to `Add data source` operation

### Delete data source

---

Click the second button in `Action` of a data source in the list to delete the data source, and the following content will pop up after clicking

![img_5.png](img_5.png)

Click the small window that pops up and click `OK` to delete the data source

!!! danger

    It should be noted that after deleting the data source, the query history related to the data source will be deleted.

### Data source management

---

Click the third button in `Action` of a data source in the list to jump to the data source management page.

![img_6.png](img_6.png)

The page is divided into left and right parts. The left side mainly displays the basic information of the data source, including:

- Database list
- Data table & data column tree list

When we select a database and a data table on the left, the content on the right is displayed as follows

![img_7.png](img_7.png)

Under the data table in the right content, you can select the previous page and next page operations of the current database table data.

On the top right we can use the sorter to reorder the data

![img_8.png](img_8.png)

When we activate the sorter, we click the `Apply` button to apply the current sorter by selecting the data column and sorting rules, and the data below will be changed according to the sorter

!!! danger

    If the current data source does not support the management function, the following page will be displayed

![img_9.png](img_9.png)
