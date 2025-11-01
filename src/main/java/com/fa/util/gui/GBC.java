package com.fa.util.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GBC extends GridBagConstraints {

	/**
	 * Constructs GBC object with grid parameters.
	 * @param gridx when in OX should the cell start.
	 * @param gridy when in OY should the cell start.
	 */
	public GBC(int gridx, int gridy) {
		this.gridx = gridx;
		this.gridy = gridy;
	}
	
	/**
	 * Constructs GBC object with grid parameters containing also their with and height.
	 * @param gridx when in OX should the cell start.
	 * @param gridy when in OY should the cell start.
	 * @param gridwidth how many columns contains the cell.
	 * @param gridheight how many rows contains the cell.
	 */
	public GBC(int gridx, int gridy, int gridwidth, int gridheight) {
		this.gridx = gridx;
		this.gridy = gridy;
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
	}


	/**
	 * Sets anchor for the GridBagLayout constraints.
	 * @param anchor anchor of the constraints
	 * @return this GBC instance
	 */
	public GBC setAnchor(int anchor) {
		this.anchor = anchor;
		return this;
	}

	/**
	 * Sets fill for the GridBagLayout constraints.
	 * @param fill fill of the constraints
	 * @return this GBC instance
	 */
	public GBC setFill(int fill) {
		this.fill = fill;
		return this;
	}

	/**
	 * Sets weight for the GridBagLayout constraints.
	 * @param weightx weight of X_AXIS of the constraints
	 * @param weighty weight of Y_AXIS of the constraints
	 * @return this GBC instance
	 */
	public GBC setWeight(double weightx, double weighty) {
		this.weightx = weightx;
		this.weighty = weighty;
		return this;
	}

	/**
	 * Sets insets for the GridBagLayout constraints, all with the same value.
	 * @param distance size of insets of the constraints
	 * @return this GBC instance
	 */
	public GBC setInsets(int distance) {
		this.insets = new Insets(distance, distance, distance, distance);
		return this;
	}

	/**
	 * Sets insets for the GridBagLayout constraints.
	 * @param top size of top inset
	 * @param left size of left inset
	 * @param bottom size of bottom inset
	 * @param right size of right inset
	 * @return this GBC instance
	 */
	public GBC setInsets(int top, int left, int bottom, int right) {
		this.insets = new Insets(top, left, bottom, right);
		return this;
	}

	/**
	 * Sets ipads for the GridBagLayout constraints.
	 * @param ipadx ipad of X_AXIS of the constraints
	 * @param ipady ipad of Y_AXIS of the constraints
	 * @return this GBC instance
	 */
	public GBC setIpad(int ipadx, int ipady) {
		this.ipadx = ipadx;
		this.ipady = ipady;
		return this;
	}	

}
