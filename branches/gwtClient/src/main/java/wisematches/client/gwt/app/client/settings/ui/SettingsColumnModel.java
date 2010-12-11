package wisematches.client.gwt.app.client.settings.ui;

/**
 * This implementation of {@code ColumnModel} saves it's properties into {@code Settings} and restore it's
 * when it's created next time.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class SettingsColumnModel { //extends ColumnModel {
//    private final String modelPrefix;
//    private final Settings settings;
//    private final ColumnConfig[] configs;
//
//    public SettingsColumnModel(String modelPrefix, Settings settings, ColumnConfig[] columnConfigs) {
//        super(initializeColumns(modelPrefix, columnConfigs, settings));
//        this.modelPrefix = modelPrefix;
//        this.configs = columnConfigs;
//        this.settings = settings;
//
//        initColumnModel();
//    }
//
//    private static ColumnConfig[] initializeColumns(String modelPrefix, ColumnConfig[] configs, Settings settings) {
//        final int[] widths = settings.getIntArray(modelPrefix + ".widths");
//        final int[] orders = settings.getIntArray(modelPrefix + ".orders");
//        final boolean[] hiddens = settings.getBooleanArray(modelPrefix + ".hiddens");
//
//        final ColumnConfig[] restoredConfigs = new ColumnConfig[configs.length];
//        for (int i = 0; i < configs.length; i++) {
//            final ColumnConfig config = configs[i];
//
//            if (widths != null && widths[i] != 0) {
//                config.setWidth(widths[i]);
//            }
//
//            if (hiddens != null) {
//                config.setHidden(hiddens[i]);
//            }
//
//            if (orders != null) {
//                restoredConfigs[orders[i]] = config;
//            } else {
//                restoredConfigs[i] = config;
//            }
//        }
//        return restoredConfigs;
//    }
//
//    private void initColumnModel() {
//        addListener(new ColumnModelListener() {
//            public void onColumnMoved(ColumnModel cm, int oldIndex, int newIndex) {
//                int[] orders = new int[configs.length];
//                for (int i = 0; i < configs.length; i++) {
//                    orders[i] = cm.getIndexById(configs[i].getId());
//                }
//                settings.setIntArray(modelPrefix + ".orders", orders);
//            }
//
//            public void onHiddenChange(ColumnModel cm, int colIndex, boolean hidden) {
//                boolean[] hiddens = new boolean[configs.length];
//                for (int i = 0; i < configs.length; i++) {
//                    hiddens[i] = cm.isHidden(configs[i].getId());
//                }
//                settings.setBooleanArray(modelPrefix + ".hiddens", hiddens);
//            }
//
//            public void onWidthChange(ColumnModel cm, int colIndex, int newWidth) {
//                int[] widths = settings.getIntArray(modelPrefix + ".widths");
//                if (widths == null) {
//                    widths = new int[configs.length];
//                }
//                for (int i = 0; i < configs.length; i++) {
//                    final String id = configs[i].getId();
//                    final int indexById = cm.getIndexById(id);
//                    if (indexById == colIndex) {
//                        widths[i] = cm.getColumnWidth(id);
//                    }
//                }
//                settings.setIntArray(modelPrefix + ".widths", widths);
//            }
//
//            public void onColumnLockChange(ColumnModel cm, int colIndex, boolean locked) {
//            }
//
//            public void onHeaderChange(ColumnModel cm, int columnIndex, String newText) {
//            }
//        });
//    }
}
