package com.civify.model.map;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class ClusterRenderer extends DefaultClusterRenderer<IssueMarker> {

    private static final int[] CLUSTER_SIZES = { 1, 2, 3 };

    public ClusterRenderer(@NonNull CivifyMap map, ClusterManager<IssueMarker> clusterManager) {
        super(map.getContext(), map.getGoogleMap(), clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(IssueMarker marker, MarkerOptions options) {
        super.onBeforeClusterItemRendered(marker, options);
        // Marker customization
        marker.setup(options);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<IssueMarker> cluster, MarkerOptions options) {
        super.onBeforeClusterRendered(cluster, options);
        // Cluster customization
    }

}
