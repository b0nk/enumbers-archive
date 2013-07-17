/*
 * This file is distributed under GPL v3 license 
 * http://www.gnu.org/licenses/gpl-3.0.html      
 *                                               
 * raven@acute-angle.net                         
 */

package org.uaraven.e;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.ninjacat.android.list.HolderCreator;
import net.ninjacat.android.list.ListViewUtils;
import net.ninjacat.android.list.UiHolder;

public class ECodeAdapter extends BaseAdapter implements HolderCreator<ECode> {

    private ECodeList list;
    private Context context;

    public ECodeAdapter(Context context, ECodeList list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        UiHolder<ECode> holder = ListViewUtils.createHolder(context, this, R.layout.ecode, convertView);
        ECode code = list.get(position);

        return holder.fillView(code);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ECode getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public UiHolder<ECode> createHolder(View view) {
        return new CodeDataHolder(view);
    }

    private static final class CodeDataHolder extends UiHolder<ECode> {

        private final View band;
        private final TextView codeId;
        private final TextView codeName;
        private final TextView codePurpose;
        private final ImageView imageVegan;
        private final ImageView imageChild;
        private final ImageView imageAllergy;

        private CodeDataHolder(View view) {
            super(view);

            band = view.findViewById(R.id.color_band);
            codeId = (TextView) view.findViewById(R.id.ecode);
            codeName = (TextView) view.findViewById(R.id.name);
            codePurpose = (TextView) view.findViewById(R.id.purpose);
            imageVegan = (ImageView) view.findViewById(R.id.vegan);
            imageChild = (ImageView) view.findViewById(R.id.child);
            imageAllergy = (ImageView) view.findViewById(R.id.allergy);
        }

        @Override
        public View fillView(ECode eCode) {
            band.setBackgroundColor(eCode.getColor());
            codeId.setText("E" + eCode.eCode);
            codeName.setText(eCode.name);
            codePurpose.setText(eCode.purpose);

            if (eCode.hasExtra()) {
                codeId.setBackgroundColor(0xFFF0F0FF);
            } else {
                codeId.setBackgroundColor(android.R.color.background_light);
            }

            imageVegan.setImageResource(eCode.vegan == 0 ? R.drawable.veg_green_small : (eCode.vegan == 2 ? R.drawable.veg_yellow_small : R.drawable.veg_red_small));
            imageChild.setImageResource(eCode.children == 0 ? R.drawable.child_green_small : R.drawable.child_red_small);
            imageAllergy.setImageResource(eCode.allergic ? R.drawable.allergic_red_small : R.drawable.allergic_green_small);

            return view;
        }


    }

}
