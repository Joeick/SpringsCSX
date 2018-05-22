package StayWavy;

import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.DisplayFrame;
import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;

public class FlyCast extends AbstractSimulation {
	// create a display frame named d
	DisplayFrame d = new DisplayFrame("X", "Y", "Cello String Simulation");

	// time
	double timeStep = .0000001;
	int counter = 0;

	// 1 meter
	// 100 heavy
	// 10 gram string
	// 100 newtons of tension
	// 200hz frequency
	// 1/1000 timestep
	// shake the end point with a sine function
	// 1cm of movement
	// rest length of each one

	// bungee variables
	double restLength = .4;
	double lineMass = .0127;
	double lineLength = .8;
	int Springs = 100;
	double kTotal = 100;
	double k = kTotal * Springs;
	double springMass = (lineMass / Springs);
	ArrayList<Spring> bungee = new ArrayList<Spring>();
	int lastSpring = Springs - 1;
	double springLength = (lineLength / Springs);
	double frequency; // 63
	double y0 = .01;
	double individualRestLength = restLength / Springs;
	FishingRod rod;

	// frequency

	@Override
	public void initialize() {
		// characteristics of the display frame
		d.setPreferredMinMax(-.1, 1.1, -.15, .15);
		d.setVisible(true);
		frequency = control.getDouble("frequency");
		// initializes the springs
		// for (int i = 0; i < Springs; i++) {
		// Spring s = new Spring(k, springMass, i * springLength, 0, 0, 0, 0, 0,
		// timeStep);
		// s.pixRadius = 3;
		// s.setXY(s.x, s.y);
		// bungee.add(s);
		// d.addDrawable(s);
		// }
		rod = new FishingRod(100, 3, 99, 1.5, .0000001);
	}

	/**
	 * Goes through code repeatedly
	 */
	protected void doStep() {
		for (int sean = 0; sean < 1000; sean++) {
			// speeding it up
			this.setDelayTime(1);

			for (int i = 0; i < rod.array.length; i++) {
				for (int j = 0; i < rod.array[i].length; i++) {
					Spring point = rod.array[i][j];
					if (i == 0 && j == 0) {
						rod.array[0]

			
					}

				}

			}

			// goes through each spring on the line
			for (int i = 0; i <= lastSpring; i++) {
				// different forces for the end springs
				// the first spring
				Spring s = bungee.get(i);
				if (i == 0) {
					s.oscY(frequency, y0, timeStep * counter);
					// s.setY(s.y);
				}
				// for all of the other springs
				else if (i <= lastSpring - 1) {
					// System.out.println(i);
					Spring sAfter = bungee.get(i + 1);
					Spring sBefore = bungee.get(i - 1);
					// a = f/m
					// FX: a =( [-k*d(i-1) * (x(i)-x(i-1))/d(i-1)] + [k*d(i) * (x(i+1)-x(i))/d(i)]
					// )/m
					// FY: a =( [-k*d(i-1) * (y(i)-y(i-1))/d(i-1)] + [k*d(i) * (x(i+1)-x(i))/d(i)]
					// )/m

					s.setAx(((-s.getK() * (s.getDistance(sBefore) - individualRestLength) * (s.getX() - sBefore.getX())
							/ s.getDistance(sBefore))
							+ s.getK() * (s.getDistance(sAfter) - individualRestLength) * (sAfter.getX() - s.getX())
									/ s.getDistance(sAfter))
							/ s.getM());

					s.setAy(((-s.getK() * (s.getDistance(sBefore) - individualRestLength) * (s.getY() - sBefore.getY())
							/ s.getDistance(sBefore))
							+ s.getK() * (s.getDistance(sAfter) - individualRestLength) * (sAfter.getY() - s.getY())
									/ s.getDistance(sAfter))
							/ s.getM());
				}
				// the last one
				else if (i == lastSpring) {
					Spring sBefore = bungee.get(i - 1);
					s.setAx(((-s.getK() * (s.getDistance(sBefore) - individualRestLength) * (s.getX() - sBefore.getX())
							/ s.getDistance(sBefore))) / s.getM());

					s.setAy(((-s.getK() * (s.getDistance(sBefore) - individualRestLength) * (s.getY() - sBefore.getY())
							/ s.getDistance(sBefore))) / s.getM());
					s.color = Color.blue;

				}
				// resets the velocity: v(t) = at+v0
				s.setVx(s.getAx() * timeStep + s.getVoldX());
				s.setVy(s.getAy() * timeStep + s.getVoldY());
				// sets the old velocity
				s.setVoldX(s.getVx());
				s.setVoldY(s.getVy());
			}
			// resets the position of each spring in the bungee
			for (int i = 0; i <= lastSpring; i++) {
				Spring s = bungee.get(i);
				s.setPosition();
				s.setXY(s.x, s.y);
			}
			counter++;
			control.println("last spring ax: " + bungee.get(lastSpring).ax);
		}
	}

	/**
	 * Resetting the values
	 */
	public void reset() {
		control.setValue("x", 0);
		control.setValue("y", 0);
		control.setValue("frequency", 63);
		d.clearDrawables();
	}

	/**
	 * Runs the program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SimulationControl.createApp(new Cello());

	}
}
