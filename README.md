

XML
-----
```xml
<de.bitninja.net.pulloutview.lib.PullOutView
    xmlns:pull="http://schemas.android.com/apk/res-auto"
    android:id="@+id/slidingLayer1"
    android:layout_width="@dimen/layer_width"
    android:layout_height="match_parent"
    pull:shadowDrawable="@drawable/sidebar_shadow"
    pull:shadowWidth="@dimen/shadow_width"
    pull:offsetWidth="@dimen/offset_width"
    pull:stickTo="auto|right|left|middle|top|bottom"
    pull:closeOnTapEnabled="true">

    …
    …
</de.bitninja.net.pulloutview.lib.PullOutView>
```

Properties:
* `shadowDrawable` - a reference to the resource drawable used to paint the shadow of the container
* `shadowWidth` - a reference to the dimension of the desired width of the given shadow
* `offsetWidth` - a reference to the dimension of the desired width for the layer to offset in the screen in order for it to be directly swipable to open
* `stickTo` - an enum that determines to where the container should stick to. ‘left’ sticks the container to the left side of the screen. ‘right’ sticks the container to the right side of the screen, and so on with ‘top‘ and ‘bottom‘ states. ‘middle’ makes the container be centered covering the whole screen and ‘auto’ makes the decision based on where you deliberately placed your container in the view. Default is ‘auto’.
* `closeOnTapEnabled` - a boolean that enables/disables the action to close the layer by tapping on an empty space of the container. Default value is true.

