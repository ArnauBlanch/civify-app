package com.civify.model.map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.civify.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class IssueMarkerClusterRenderer extends DefaultClusterRenderer<IssueMarker> {

    private static final int[] CLUSTER_THRESHOLDS = {10, 30, 60, 100};

    private CivifyMap mMap;
    private ClusterManager<IssueMarker> mClusterManager;
    private final IconGenerator mClusterIconGenerator;

    public IssueMarkerClusterRenderer(@NonNull CivifyMap map, ClusterManager<IssueMarker> manager) {
        super(map.getContext(), map.getGoogleMap(), manager);
        mMap = map;
        mClusterManager = manager;
        mClusterIconGenerator = new IconGenerator(map.getContext());
    }

    @Override
    protected void onBeforeClusterItemRendered(IssueMarker issueMarker, MarkerOptions options) {
        super.onBeforeClusterItemRendered(issueMarker, options);
        // Marker customization
        issueMarker.setup(options);
    }

    @Override
    protected void onClusterItemRendered(IssueMarker issueMarker, Marker marker) {
        super.onClusterItemRendered(issueMarker, marker);
        // Notify rendered
        issueMarker.render(marker, mClusterManager);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<IssueMarker> cluster, MarkerOptions options) {
        super.onBeforeClusterRendered(cluster, options);
        // Cluster customization
        Context context = mMap.getContext();
        int clusterSize = cluster.getSize();
        String clusterIconRes = "cluster1";
        for (int i = 0; i < CLUSTER_THRESHOLDS.length; i++) {
            int threshold = CLUSTER_THRESHOLDS[i];
            if (clusterSize >= threshold) clusterIconRes = "cluster" + (i + 2);
        }
        mClusterIconGenerator.setBackground(getClusterIcon(context, clusterIconRes));
        mClusterIconGenerator.setTextAppearance(R.style.ClusterIconText);
        Bitmap sizeIcon = mClusterIconGenerator.makeIcon(String.valueOf(clusterSize));
        options.icon(BitmapDescriptorFactory.fromBitmap(sizeIcon));
    }

    private static Drawable getClusterIcon(Context context, String res9patchName) {
        Resources resources = context.getResources();
        return ContextCompat.getDrawable(context,
                resources.getIdentifier(res9patchName, "drawable", context.getPackageName()));
    }

}
