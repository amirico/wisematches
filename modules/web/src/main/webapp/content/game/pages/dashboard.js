Ext.onReady(function() {
// sample static data for the store
    /**
     * Custom function used for column renderer
     * @param {Object} val
     */
    function change(val) {
        if (val > 0) {
            return '<span style="color:green;">' + val + '</span>';
        } else if (val < 0) {
            return '<span style="color:red;">' + val + '</span>';
        }
        return val;
    }

    /**
     * Custom function used for column renderer
     * @param {Object} val
     */
    function pctChange(val) {
        if (val > 0) {
            return '<span style="color:green;">' + val + '%</span>';
        } else if (val < 0) {
            return '<span style="color:red;">' + val + '%</span>';
        }
        return val;
    }

    var reader = new Ext.data.JsonReader({
        idProperty:'boardId',
        fields: [
            {name: 'boardId', type: 'int'},
            {name: 'title', mapping: 'gameSettings.title', ype: 'string'},
            {name: 'language', mapping: 'gameSettings.language', ype: 'string'}
        ]
        // additional configuration for remote
//        root:'boards'
    });

    // create the data store
    var store = new Ext.data.Store({
        reader: reader,
        // use remote data
        proxy : new Ext.data.HttpProxy({
            url: '/game/dashboard/active.html',
            method: 'GET'
        })
//        sortInfo: {field: 'tile', direction: 'ASC'}
//        groupField: 'project'
    });

    // manually load local data
    store.load();

// create the Grid
    var grid = new Ext.grid.GridPanel({
        width: '100%',
        autoHeight: true,
        store: store,
        columns: [
            {
                id       :'title',
                header   : 'Title',
                width    : 160,
                sortable : true,
                dataIndex: 'title'
            },
            {
                id       :'language',
                header   : 'Language',
                width    : 160,
                sortable : true,
                dataIndex: 'language'
            }
        ],
        stripeRows: true,
        autoExpandColumn: 'title',
        stateful: true,
        stateId: 'dashboardGrid'
    });

    // render the grid to the specified div in the page
    grid.render('dashboard-grid');
});
