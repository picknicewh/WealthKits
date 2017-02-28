package com.cfjn.javacf.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.cfjn.javacf.util.TitleValueColorEntity;
import com.cfjn.javacf.util.AngleUtils;
import com.cfjn.javacf.util.G;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 * 作者： wh
 * 时间： 2016/6/29
 * 名称：记账---饼图
 * 版本说明：
 * 附加注释：
 * 主要接口：无
 */
@SuppressLint("NewApi")
public class PieChartView extends View implements Runnable {
	private static final String TAG = "Thread";
	/**
	 * 饼图是否正在旋转
	 */
	private boolean isRotating;
	/**
	 * 饼图绘制的初始角度
	 */
	private Angle offset = new Angle(-90);
	/**
	 * 选中时
	 */
	private int selectedAngle = 90;
	/**
	 * 上下文
	 */
	private Context context;
	/**
	 * 屏幕宽度
	 */
	public int screenWith;
	/**
	 * 饼图圆心
	 */
	private Point position;
	/**
	 * 饼图半径长度
	 */
	private int radiusLength;
	/**
	 * 画笔
	 */
	private Paint paint;
	/**
	 * 饼图中的数据集合，包括名字、颜色和数据大小
	 */
	private List<TitleValueColorEntity> data;
	/**
	 * 饼图中的弧的集合
	 */
	private List<Item> items = new ArrayList<Item>();

	/**
	 * 饼图构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param attrs
	 *            参数集合
	 */
	public PieChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		screenWith =  G.size.W;
		new Thread(this).start();
		Log.i(TAG, "线程多了一个");
	}

	public boolean isRotate() {
		return isRotating;
	}

	public void setRotate(boolean isRotate) {
		this.isRotating = isRotate;
	}

	public int getOffset() {
		return offset.getDegree();

	}

	public void setOffset(int offset) {
		this.offset.setDegree(offset);
	}

	public void setData(List<TitleValueColorEntity> data) {
		this.data = data;
		// 设置数据后，刷新界面
		setItem();
		invalidate();
	}

	/**
	 * 设置Item
	 */
	private void setItem() {
		items.clear();
		Item item;
		// 将数据放入item
		for (TitleValueColorEntity entity : data) {
			item = new Item(entity);
			items.add(item);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				drawSelected((Item) msg.obj);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 重写onDraw方法
	 *            画布
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension((int) (screenWith * 0.8), (int) (screenWith * 0.8));
	};


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 画安全矩形
		int rect = getWidth() < getHeight() ? getWidth() : getHeight();
		// 设定半径长度
		radiusLength = (int) ((int) (rect / 2f) * 0.9);
		// 设置圆心
		if(position == null){
			position = new Point(getWidth() / 2, getHeight() / 2);
		}
		drawItems(canvas);
		drawCircle(canvas);
	}

	private boolean isFirstDraw = true;

	/**
	 * 画每一段弧
	 * 
	 * @param canvas
	 *            画布对象
	 */
	private void drawItems(Canvas canvas) {
		// 如果数据不为空
		if (data != null) {
			// 得到画笔
			paint = new Paint();
			// 设置抗锯齿
			paint.setAntiAlias(true);
			// 数据中数值的总和
			float sum = 0;
			// 设置弧外矩形
			RectF oval = new RectF(position.x - radiusLength, position.y
					- radiusLength, position.x + radiusLength, position.y
					+ radiusLength);
			// 设置内圆外矩形
			RectF interOval = new RectF(
					position.x - (int) (radiusLength * 0.5), position.y
							- (int) (radiusLength * 0.5) + screenWith / 100,
					position.x + (int) (radiusLength * 0.5), position.y
							+ (int) (radiusLength * 0.5) + screenWith / 100);

			// 计算数值总和
			for (TitleValueColorEntity entity : data) {
				sum += entity.getValue();
			}
			sumSweep = 0;
			// 得到饼图起始位置
			int offset = this.offset.getDegree();
			// 画每一段弧
			for (TitleValueColorEntity entity : data) {
				// 设置弧颜色
				paint.setColor(entity.getColor());
				// 弧扫过的角度

				int i = data.indexOf(entity);
				int sweep;
				if (i != data.size() - 1) {
					sweep = Math.round(entity.getValue() / sum * 360f);
					sumSweep += sweep;
				} else {
					sweep = 360 - sumSweep;
				}
				// 设置这段弧的百分比
				items.get(i).setPercentage(entity.getValue() / (float) sum);
				// 设置这段弧的起始位置
				items.get(i).setStart(AngleUtils.formatAngel(offset));

				// 如果这段弧被选中，且饼图没有旋转
				if (items.get(i).isSelected() && !isRotate()
						&& items.size() > 1) {
					// 设置一个新弧外矩形
					RectF oval1 = new RectF(position.x - radiusLength,
							position.y - radiusLength + screenWith / 100,
							position.x + radiusLength, position.y
									+ radiusLength + screenWith / 100);
					paint.setStyle(Paint.Style.FILL_AND_STROKE);
					// 画弧
					canvas.drawArc(oval1, offset, sweep, true, paint);
					paint.setColor(Color.WHITE);
					// 画内部弧
					canvas.drawArc(interOval, offset, sweep, true, paint);
				} else {
					// 画弧
					canvas.drawArc(oval, offset, sweep, true, paint);
				}
				// 重新计算初始位置
				offset += sweep;
				// 设置弧的结束位置
				items.get(i).setEnd(AngleUtils.formatAngel(offset));
			}
			if (isFirstDraw) {
				selected(items.get(selectedPosition));
				isFirstDraw = false;
			}
		}
      Log.i("TAG","pie");
	}

	private int sumSweep = 0;

	/**
	 * 画内圆
	 * 
	 * @param canvas
	 *            画布
	 */
	private void drawCircle(Canvas canvas) {
		paint = new Paint();
		// 抗锯齿
		paint.setAntiAlias(true);
		// 设置画笔颜色为白色
		paint.setColor(Color.WHITE);
		// 画圆
		canvas.drawCircle(position.x, position.y, radiusLength / 2, paint);
	}

	/**
	 * 当前坐标x
	 */
	float x = 0.0f;
	/**
	 * 当前坐标Y
	 */
	float y = 0.0f;
	/**
	 * 手指按下时坐标x
	 */
	float downX = 0.0f;
	/**
	 * 手指按下时坐标y
	 */
	float downY = 0.0f;
	/**
	 * 上次坐标x
	 */
	float lastX = 0.0f;
	/**
	 * 上次坐标y
	 */
	float lastY = 0.0f;
	/**
	 * 起始时间
	 */
	long startTime = 0;
	/**
	 * 结束时间
	 */
	long endTime = 0;
	/**
	 * 上次按下时间
	 */
	long lastDownTime = 0;
	/**
	 * 是否可以进行单击
	 */
	boolean isSingleTap;

	long dTime;
	long currentMoveTime;
	/**
	 * 上次移动时间
	 */
	long lastMoveTime;
	/**
	 * 是否可以移动
	 */
	boolean isMove;
	int isClockWise;
	int lastClockWise;
	int sumDAngle = 0;

	public void setDisallowInterceptTouch(ViewGroup viewGroup) {
		disallowViewGroup = viewGroup;
	}

	private ViewGroup disallowViewGroup;

	PointF downPoint = new PointF();

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (disallowViewGroup != null) {
				disallowViewGroup.requestDisallowInterceptTouchEvent(true);
			}
			setStopRoter(true);
			sumDAngle = 0;
			// sumDAngle = 0;
			// 记录按下时间
			startTime = System.currentTimeMillis();
			downX = lastX = x = event.getX();
			downY = lastY = y = event.getY();
			downPoint.x = event.getX();
			downPoint.y = event.getY();
			// 设置为可以单击
			isSingleTap = true;
			// 记录上次按下时间
			lastDownTime = startTime;
			int angle = 0;
			isMove = true;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			isMove = false;
			break;

		case MotionEvent.ACTION_MOVE:
			if (isMove) {
				x = event.getX();
				y = event.getY();

				if ((x - position.x) * (x - position.x) + (y - position.y)
						* (y - position.y) > radiusLength * radiusLength * 0.25) {
					if ((PointF.length(event.getX() - downPoint.x, event.getY()
							- downPoint.y) >= (float) 5.0)) {
						// 将弧全部设置为未选中
						for (Item item : items) {
							item.setSelected(false);
						}
						// 计算角度差
						int dAngle = AngleUtils.formatAngel(caculateAngle(x, y,
								position.x, position.y)
								- caculateAngle(lastX, lastY, position.x,
										position.y));
						if ((dAngle >= 0 && isClockWise >= 0)
								|| (dAngle <= 0 && isClockWise <= 0)) {
							sumDAngle += dAngle;
						} else {
							sumDAngle = dAngle;
						}
						if (dAngle > 0) {
							isClockWise = 1;
						} else if (dAngle < 0) {
							isClockWise = -1;
						}

						// 设置饼图起始位置
						this.offset
								.setDegree((int) (this.offset.getDegree() + dAngle));
						invalidate();
						if (x != lastX && y != lastY) {
							lastMoveTime = System.currentTimeMillis();
						}
						lastX = x;
						lastY = y;
					}
				} else {
					lastX = event.getX();
					lastY = event.getY();
				}
			}
			// Log.i(ThreeTAG, "dangle" + String.valueOf(dAngle));
			// Log.i(ThreeTAG, "sumDangle" + String.valueOf(sumDAngle));
			break;
		case MotionEvent.ACTION_UP:
			if (disallowViewGroup != null) {
				disallowViewGroup.requestDisallowInterceptTouchEvent(false);
			}
			x = event.getX();
			y = event.getY();
			endTime = System.currentTimeMillis();
			long dTime = endTime - startTime;
			angle = caculateAngle(x, y, position.x, position.y);
			// 如果在大圆内且在小圆外
			if ((x - position.x) * (x - position.x) + (y - position.y)
					* (y - position.y) < radiusLength * radiusLength
					&& (x - position.x) * (x - position.x) + (y - position.y)
							* (y - position.y) > radiusLength * radiusLength
							* 0.25) {
				// 如果单击时间小于八百且x，y不变
				if (dTime < 800
						&& (PointF.length(event.getX() - downPoint.x,
								event.getY() - downPoint.y) < (float) 5.0)) {
					// 是否在弧中
					boolean isInItem;
					for (Item item : items) {
						isInItem = item.inItem(angle);
						if (isInItem && !item.isSelected()) {
							// 如果没有在旋转且可以单击
							if (!isRotate() && isSingleTap && isStopRoter())
								onSingleTap(item);
							break;
						}
					}
				} else {
					int dAngle = caculateAngle(x, y, position.x, position.y)
							- caculateAngle(downX, downY, position.x,
									position.y);

					if (dAngle > 0 && isClockWise < 0) {
						dAngle = dAngle - 360;
					} else if (dAngle < 0 && isClockWise > 0) {
						dAngle = dAngle + 360;
					}
					currentMoveTime = System.currentTimeMillis();
					dTime = currentMoveTime - lastDownTime;
					long dMoveTime = currentMoveTime - lastMoveTime;
					double speed = (sumDAngle / (double) dTime) * 10;
					sumDAngle = (int) speed * 200;
					if (sumDAngle < 200 && sumDAngle > 0) {
						sumDAngle = 200;
					}
					if (sumDAngle > -200 && sumDAngle < 0) {
						sumDAngle = -200;
					}
					if (sumDAngle > 720) {
						sumDAngle = 720;
					}
					if (sumDAngle < -720) {
						sumDAngle = -720;
					}
					if (dMoveTime > 800) {
						sumDAngle = 0;
					}
					// startAnim(items.get(1),(int)(speed*200));
					setStopRoter(false);
					// for (Item item : items) {
					// boolean isInItem;
					// isInItem = item.inItem(selectedAngle);
					//
					// if (isInItem) {
					// // while(!isDirty())
					//
					// selected(item);
					// }
					// isSingleTap = false;
					// }
				}
			} else {
				int dAngle = caculateAngle(x, y, position.x, position.y)
						- caculateAngle(downX, downY, position.x, position.y);
				if (dAngle > 0 && isClockWise < 0) {
					dAngle = dAngle - 360;
				} else if (dAngle < 0 && isClockWise > 0) {
					dAngle = dAngle + 360;

					// startAnim(items.get(1),(int)(speed*200));

				}
				currentMoveTime = System.currentTimeMillis();
				dTime = currentMoveTime - lastDownTime;
				double speed = (sumDAngle / (double) dTime) * 10;
				sumDAngle = (int) speed * 200;
				if (sumDAngle < 200 && sumDAngle > 0) {
					sumDAngle = 200;
				}
				if (sumDAngle > -200 && sumDAngle < 0) {
					sumDAngle = -200;
				}
				if (sumDAngle > 720) {
					sumDAngle = 720;
				}
				if (sumDAngle < -720) {
					sumDAngle = -720;
				}
				long dMoveTime = currentMoveTime - lastMoveTime;
				if (dMoveTime > 800) {
					sumDAngle = 0;
				}

				setStopRoter(false);
				lastX = 0;
				lastY = 0;
				x = 0;
				y = 0;
				isMove = false;
			}
			isClockWise = 0;

		default:
			isMove = false;
			break;
		}
		return true;
	}

	private void onSingleTap(Item item) {
		for (Item item1 : items) {
			item1.setSelected(false);
			// Log.i(TAG,"onsingleTap:isselected" + "$$$$"+
			// String.valueOf(item1.isSelected()));
		}
		selected(item);
	}

	private void selected(Item item) {

		startAnim(item, 0);

	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
	}

	boolean isAnimEnd;

	private synchronized void drawSelected(Item item) {

		for (Item item1 : items) {
			item1.setSelected(false);
		}
		item.setSelected(true);
		notifySelectedListeners(items.indexOf(item), item.getData().getTitle(),
				item.getData().getValue(), item.getPercentage());
		postInvalidate();

	}

	private int selectedPosition;

	public void selectedItem(int i, boolean isFirstDraw) {
		selectedPosition = i;
		this.isFirstDraw = isFirstDraw;
	}

	private synchronized void startAnim(final Item item, final int angle) {
		Thread animThread = new Thread(new Runnable() {
			@Override
			public void run() {
				boolean isAnimEnd = false;

				if (android.os.Build.VERSION.SDK_INT >= 11) {
					boolean isWaiting = isDirty();
					while (isWaiting) {
						isWaiting = isDirty();
						try {
							Thread.sleep(3);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} else {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				setRotate(true);
				int dangle = 0;
				if (angle == 0) {
					dangle = AngleUtils.formatAngel(selectedAngle
							- item.getMiddle());
				} else {
					dangle = angle;
				}
				int offset = getOffset();
				int sum = Math.abs(dangle);
				int j = 0;
				int count = 0;
				while (sum > 0) {
					j++;
					sum = sum - j;

					count++;
				}
				count = count - 1 + (sum + j);
				int v = j - 1;
				// for (int i = 0; i < Math.abs(dangle); i++) {
				for (int i = 0; i < count; i++) {
					if (dangle > 0) {
						offset += v;
					} else {
						offset -= v;
					}
					if (v > 1) {
						v--;
					}
					setOffset(offset);
					postInvalidate();
					try {
						Thread.sleep(10);
						// Thread.sleep(100 / Math.abs(dangle));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
				setRotate(false);
				// selected(item);
				// drawSelected(item);
				Message message = new Message();
				message.what = 0;
				message.obj = item;
				handler.sendMessage(message);

			}
		});
		animThread.start();
	}

	private int caculateAngle(float x, float y, int pointX, int pointY) {

		float dx = x - pointX;
		float dy = y - pointY;
		int angle = Math.round((float) Math.toDegrees(Math.atan2(dy, dx)));
		return angle;
	}

	private boolean stopRoter = true;

	public boolean isStopRoter() {
		return stopRoter;
	}

	public void setStopRoter(boolean stopRoter) {
		this.stopRoter = stopRoter;
	}

	int v = 20;
	private boolean isRun = true;

	public void setRun(boolean isRun) {
		this.isRun = isRun;
		if(isRun){
			new Thread(this).start();
			Log.i(TAG, "线程多了一个");
		}
		
	}

	public void run() {
		try {
			while (isRun) {
				if (!isStopRoter()) {
					for (Item item1 : items) {
						item1.setSelected(false);
					}
					if (sumDAngle < 10 && sumDAngle > -10) {
						setStopRoter(true);
						sumDAngle = 0;
						v = 20;
						Thread.sleep(50);
						for (Item item : items) {

							boolean isInItem;
							isInItem = item.inItem(selectedAngle);

							if (isInItem) {
								selected(item);

							}
							isSingleTap = false;
						}
					} else {

						if (sumDAngle > 0) {

							offset.setDegree(offset.getDegree() + v);
							sumDAngle -= v;

						} else {
							offset.setDegree(offset.getDegree() - v);
							sumDAngle += v;

						}
						this.postInvalidate();
						v--;
						if (v < 3) {
							v = 3;
						}
						Thread.sleep(10);
					}
				}
			}

		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		Log.i(TAG, "线程少了一个");
	}

	private List<OnPieChartItemSelectedListener> itemSelectedListeners = new LinkedList();

	public void setOnItemSelectedListener(
			OnPieChartItemSelectedListener listener) {
		this.itemSelectedListeners.add(listener);
	}

	public void removeItemSelectedListener(
			OnPieChartItemSelectedListener listener) {
		this.itemSelectedListeners.remove(listener);
	}

	protected void notifySelectedListeners(int position, String title,
			float value, float percent) {
		for (OnPieChartItemSelectedListener listener : this.itemSelectedListeners)
			listener.onPieChartItemSelected(this, position, title, value,
					percent);
	}
}

class Item {
	private int start;
	private int end;
	private boolean isSelected;
	private TitleValueColorEntity data;
	private float percentage;

	public Item(TitleValueColorEntity data) {
		super();
		this.data = data;
	}

	public boolean inItem(int angle) {
		if (end - start < 0) {
			if (angle >= start && angle < 180 || angle < end && angle > -180) {
				return true;
			}
			return false;
		}
		if (start <= angle && angle < end) {
			return true;
		}
		return false;
	}

	public int getMiddle() {
		if (end - start > 0) {
			return Math.round((end + start) / 2f);
		}
		return AngleUtils.formatAngel(Math.round((end + start) / 2f) + 180);
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public TitleValueColorEntity getData() {
		return data;
	}

	public void setData(TitleValueColorEntity data) {
		this.data = data;
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
}

class Angle {
	private int degree;

	public Angle(int degree) {
		super();
		this.degree = degree;
	}

	public int getDegree() {
		return AngleUtils.formatAngel(degree);
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}
}
