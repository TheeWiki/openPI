// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import sign.signlink;
import java.awt.*;
import java.awt.image.PixelGrabber;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import javax.swing.ImageIcon;

public final class Sprite extends DrawingArea {

	public Sprite(int i, int j) {
		myPixels = new int[i * j];
		myWidth = anInt1444 = i;
		myHeight = anInt1445 = j;
		anInt1442 = anInt1443 = 0;
	}
	
	public void drawSprite3(int x, int y, int opacity) {
		int alpha = opacity;
		x += this.anInt1442;// offsetX
		y += this.anInt1443;// offsetY
		int destOffset = x + y * DrawingArea.width;
		int srcOffset = 0;
		int height = this.myHeight;
		int width = this.myWidth;
		int destStep = DrawingArea.width - width;
		int srcStep = 0;
		if (y < DrawingArea.topY) {
			int trimHeight = DrawingArea.topY - y;
			height -= trimHeight;
			y = DrawingArea.topY;
			srcOffset += trimHeight * width;
			destOffset += trimHeight * DrawingArea.width;
		}
		if (y + height > DrawingArea.bottomY) {
			height -= (y + height) - DrawingArea.bottomY;
		}
		if (x < DrawingArea.topX) {
			int trimLeft = DrawingArea.topX - x;
			width -= trimLeft;
			x = DrawingArea.topX;
			srcOffset += trimLeft;
			destOffset += trimLeft;
			srcStep += trimLeft;
			destStep += trimLeft;
		}
		if (x + width > DrawingArea.bottomX) {
			int trimRight = (x + width) - DrawingArea.bottomX;
			width -= trimRight;
			srcStep += trimRight;
			destStep += trimRight;
		}
		if (!((width <= 0) || (height <= 0))) {
			setPixels(width, height, DrawingArea.pixels, myPixels, alpha, destOffset, srcOffset, destStep, srcStep);
		}
	}
	
	private void setPixels(int width, int height, int destPixels[], int srcPixels[], int srcAlpha, int destOffset, int srcOffset, int destStep, int srcStep) {
		int srcColor;
		int destAlpha;
		int rofl = srcAlpha;
		for (int loop = -height; loop < 0; loop++) {
			for (int loop2 = -width; loop2 < 0; loop2++) {
				srcAlpha = ((this.myPixels[srcOffset] >> 24) & rofl);
				destAlpha = 256 - srcAlpha;
				srcColor = srcPixels[srcOffset++];
				if (srcColor != 0 && srcColor != 0xffffff) {
					int destColor = destPixels[destOffset];
					destPixels[destOffset++] = ((srcColor & 0xff00ff) * srcAlpha
						+ (destColor & 0xff00ff) * destAlpha & 0xff00ff00)
						+ ((srcColor & 0xff00) * srcAlpha + (destColor & 0xff00) * destAlpha & 0xff0000) >> 8;
				} else {
					destOffset++;
				}
			}
			destOffset += destStep;
			srcOffset += srcStep;
		}
	}

	public Sprite(byte abyte0[], Component component) {
		try {
			Image image = Toolkit.getDefaultToolkit().createImage(abyte0);
			MediaTracker mediatracker = new MediaTracker(component);
			mediatracker.addImage(image, 0);
			mediatracker.waitForAll();
			myWidth = image.getWidth(component);
			myHeight = image.getHeight(component);
			anInt1444 = myWidth;
			anInt1445 = myHeight;
			anInt1442 = 0;
			anInt1443 = 0;
			myPixels = new int[myWidth * myHeight];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
			pixelgrabber.grabPixels();
		} catch(Exception _ex) {
			_ex.printStackTrace();
		}
	}

	public Sprite(byte abyte0[], int width, int height) {
		try {
			Image image = Toolkit.getDefaultToolkit().createImage(abyte0);
			myWidth = width;
			myHeight = height;
			anInt1444 = myWidth;
			anInt1445 = myHeight;
			anInt1442 = 0;
			anInt1443 = 0;
			myPixels = new int[myWidth * myHeight];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
			pixelgrabber.grabPixels();
		} catch(Exception _ex) {
			_ex.printStackTrace();
		}
	}
	
	public void setTransparency(int transRed, int transGreen, int transBlue)
	{
		for(int index = 0; index < myPixels.length; index++)
			if(((myPixels[index] >> 16) & 255) == transRed && ((myPixels[index] >> 8) & 255) == transGreen && (myPixels[index] & 255) == transBlue)
				myPixels[index] = 0;
	}
	
	public Sprite(StreamLoader streamLoader, String s, int i)
	{
		Stream stream = new Stream(streamLoader.getDataForName(s + ".dat"));
		Stream stream_1 = new Stream(streamLoader.getDataForName("index.dat"));
		stream_1.currentOffset = stream.readUnsignedWord();
		anInt1444 = stream_1.readUnsignedWord();
		anInt1445 = stream_1.readUnsignedWord();
		int j = stream_1.readUnsignedByte();
		int ai[] = new int[j];
		for(int k = 0; k < j - 1; k++)
		{
			ai[k + 1] = stream_1.read3Bytes();
			if(ai[k + 1] == 0)
				ai[k + 1] = 1;
		}

		for(int l = 0; l < i; l++)
		{
			stream_1.currentOffset += 2;
			stream.currentOffset += stream_1.readUnsignedWord() * stream_1.readUnsignedWord();
			stream_1.currentOffset++;
		}

		anInt1442 = stream_1.readUnsignedByte();
		anInt1443 = stream_1.readUnsignedByte();
		myWidth = stream_1.readUnsignedWord();
		myHeight = stream_1.readUnsignedWord();
		int i1 = stream_1.readUnsignedByte();
		int j1 = myWidth * myHeight;
		myPixels = new int[j1];
		if(i1 == 0)
		{
			for(int k1 = 0; k1 < j1; k1++)
				myPixels[k1] = ai[stream.readUnsignedByte()];
			setTransparency(255, 0, 255);
			return;
		}
		if(i1 == 1)
		{
			for(int l1 = 0; l1 < myWidth; l1++)
			{
				for(int i2 = 0; i2 < myHeight; i2++)
					myPixels[l1 + i2 * myWidth] = ai[stream.readUnsignedByte()];
			}

		}
		setTransparency(255, 0, 255);
	}
	
	public Sprite(StreamLoader streamLoader, int width, int height) {
		try {
			Image image = Toolkit.getDefaultToolkit().createImage(EMPTY);
			myWidth = width;
			myHeight = height;
			anInt1444 = myWidth;
			anInt1445 = myHeight;
			anInt1442 = 0;
			anInt1443 = 0;
			myPixels = new int[myWidth * myHeight];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
			pixelgrabber.grabPixels();
			image = null;
			setTransparency(255, 0, 255);
		} catch(Exception e) {
		}
	}
	
	public Sprite(String s, int width, int height) {
		try {
			Image image = Toolkit.getDefaultToolkit().createImage(HITPOINTS);
			myWidth = width;
			myHeight = height;
			anInt1444 = myWidth;
			anInt1445 = myHeight;
			anInt1442 = 0;
			anInt1443 = 0;
			myPixels = new int[myWidth * myHeight];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
			pixelgrabber.grabPixels();
			image = null;
			setTransparency(255, 0, 255);
			setTransparency(255, 255, 255);
		} catch(Exception e) {
		}
	}

	public void method343()
	{
		DrawingArea.initDrawingArea(myHeight, myWidth, myPixels);
	}

	public void method344(int i, int j, int k)
	{
		for(int i1 = 0; i1 < myPixels.length; i1++)
		{
			int j1 = myPixels[i1];
			if(j1 != 0)
			{
				int k1 = j1 >> 16 & 0xff;
				k1 += i;
				if(k1 < 1)
					k1 = 1;
				else
				if(k1 > 255)
					k1 = 255;
				int l1 = j1 >> 8 & 0xff;
				l1 += j;
				if(l1 < 1)
					l1 = 1;
				else
				if(l1 > 255)
					l1 = 255;
				int i2 = j1 & 0xff;
				i2 += k;
				if(i2 < 1)
					i2 = 1;
				else
				if(i2 > 255)
					i2 = 255;
				myPixels[i1] = (k1 << 16) + (l1 << 8) + i2;
			}
		}

	}

	public void method345()
	{
		int ai[] = new int[anInt1444 * anInt1445];
		for(int j = 0; j < myHeight; j++)
		{
			System.arraycopy(myPixels, j * myWidth, ai, j + anInt1443 * anInt1444 + anInt1442, myWidth);
		}

		myPixels = ai;
		myWidth = anInt1444;
		myHeight = anInt1445;
		anInt1442 = 0;
		anInt1443 = 0;
	}

	public void method346(int i, int j)
	{
		i += anInt1442;
		j += anInt1443;
		int l = i + j * DrawingArea.width;
		int i1 = 0;
		int j1 = myHeight;
		int k1 = myWidth;
		int l1 = DrawingArea.width - k1;
		int i2 = 0;
		if(j < DrawingArea.topY)
		{
			int j2 = DrawingArea.topY - j;
			j1 -= j2;
			j = DrawingArea.topY;
			i1 += j2 * k1;
			l += j2 * DrawingArea.width;
		}
		if(j + j1 > DrawingArea.bottomY)
			j1 -= (j + j1) - DrawingArea.bottomY;
		if(i < DrawingArea.topX)
		{
			int k2 = DrawingArea.topX - i;
			k1 -= k2;
			i = DrawingArea.topX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if(i + k1 > DrawingArea.bottomX)
		{
			int l2 = (i + k1) - DrawingArea.bottomX;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if(k1 <= 0 || j1 <= 0)
		{
		} else
		{
			method347(l, k1, j1, i2, i1, l1, myPixels, DrawingArea.pixels);
		}
	}

	private void method347(int i, int j, int k, int l, int i1, int k1,
						   int ai[], int ai1[])
	{
		int l1 = -(j >> 2);
		j = -(j & 3);
		for(int i2 = -k; i2 < 0; i2++)
		{
			for(int j2 = l1; j2 < 0; j2++)
			{
				ai1[i++] = ai[i1++];
				ai1[i++] = ai[i1++];
				ai1[i++] = ai[i1++];
				ai1[i++] = ai[i1++];
			}

			for(int k2 = j; k2 < 0; k2++)
				ai1[i++] = ai[i1++];

			i += k1;
			i1 += l;
		}
	}

	public void drawSprite1(int i, int j)
	{
		int k = 128;//was parameter
		i += anInt1442;
		j += anInt1443;
		int i1 = i + j * DrawingArea.width;
		int j1 = 0;
		int k1 = myHeight;
		int l1 = myWidth;
		int i2 = DrawingArea.width - l1;
		int j2 = 0;
		if(j < DrawingArea.topY)
		{
			int k2 = DrawingArea.topY - j;
			k1 -= k2;
			j = DrawingArea.topY;
			j1 += k2 * l1;
			i1 += k2 * DrawingArea.width;
		}
		if(j + k1 > DrawingArea.bottomY)
			k1 -= (j + k1) - DrawingArea.bottomY;
		if(i < DrawingArea.topX)
		{
			int l2 = DrawingArea.topX - i;
			l1 -= l2;
			i = DrawingArea.topX;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if(i + l1 > DrawingArea.bottomX)
		{
			int i3 = (i + l1) - DrawingArea.bottomX;
			l1 -= i3;
			j2 += i3;
			i2 += i3;
		}
		if(!(l1 <= 0 || k1 <= 0))
		{
			method351(j1, l1, DrawingArea.pixels, myPixels, j2, k1, i2, k, i1);
		}
	}
	
	public void drawTransparentSprite(int i, int j, int opacity)
	{
		int k = opacity;//was parameter
		i += anInt1442;
		j += anInt1443;
		int i1 = i + j * DrawingArea.width;
		int j1 = 0;
		int k1 = myHeight;
		int l1 = myWidth;
		int i2 = DrawingArea.width - l1;
		int j2 = 0;
		if(j < DrawingArea.topY)
		{
			int k2 = DrawingArea.topY - j;
			k1 -= k2;
			j = DrawingArea.topY;
			j1 += k2 * l1;
			i1 += k2 * DrawingArea.width;
		}
		if(j + k1 > DrawingArea.bottomY)
			k1 -= (j + k1) - DrawingArea.bottomY;
		if(i < DrawingArea.topX)
		{
			int l2 = DrawingArea.topX - i;
			l1 -= l2;
			i = DrawingArea.topX;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if(i + l1 > DrawingArea.bottomX)
		{
			int i3 = (i + l1) - DrawingArea.bottomX;
			l1 -= i3;
			j2 += i3;
			i2 += i3;
		}
		if(!(l1 <= 0 || k1 <= 0))
		{
			method351(j1, l1, DrawingArea.pixels, myPixels, j2, k1, i2, k, i1);
		}
	}

	public void drawSprite(int i, int k)
	{
		i += anInt1442;
		k += anInt1443;
		int l = i + k * DrawingArea.width;
		int i1 = 0;
		int j1 = myHeight;
		int k1 = myWidth;
		int l1 = DrawingArea.width - k1;
		int i2 = 0;
		if(k < DrawingArea.topY)
		{
			int j2 = DrawingArea.topY - k;
			j1 -= j2;
			k = DrawingArea.topY;
			i1 += j2 * k1;
			l += j2 * DrawingArea.width;
		}
		if(k + j1 > DrawingArea.bottomY)
			j1 -= (k + j1) - DrawingArea.bottomY;
		if(i < DrawingArea.topX)
		{
			int k2 = DrawingArea.topX - i;
			k1 -= k2;
			i = DrawingArea.topX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if(i + k1 > DrawingArea.bottomX)
		{
			int l2 = (i + k1) - DrawingArea.bottomX;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if(!(k1 <= 0 || j1 <= 0))
		{
			method349(DrawingArea.pixels, myPixels, i1, l, k1, j1, l1, i2);
		}
	}

	public void drawSprite(int i, int k, int color) {
		int tempWidth = myWidth + 2;
		int tempHeight = myHeight + 2;
		int[] tempArray = new int[tempWidth * tempHeight];
		for(int x = 0; x < myWidth; x++) {
			for(int y = 0; y < myHeight; y++) {
				if(myPixels[x + y * myWidth] != 0)
					tempArray[(x + 1) + (y + 1) * tempWidth] = myPixels[x + y * myWidth];
			}
		}
		for(int x = 0; x < tempWidth; x++) {
			for(int y = 0; y < tempHeight; y++) {
				if(tempArray[(x) + (y) * tempWidth] == 0) {
					if(x < tempWidth - 1 && tempArray[(x + 1) + ((y) * tempWidth)] > 0 && tempArray[(x + 1) + ((y) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
					if(x > 0 && tempArray[(x - 1) + ((y) * tempWidth)] > 0 && tempArray[(x - 1) + ((y) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
					if(y < tempHeight - 1 && tempArray[(x) + ((y + 1) * tempWidth)] > 0 && tempArray[(x) + ((y + 1) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
					if(y > 0 && tempArray[(x) + ((y - 1) * tempWidth)] > 0 && tempArray[(x) + ((y - 1) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
				}
			}
		}
		i--;
		k--;
		i += anInt1442;
		k += anInt1443;
		int l = i + k * DrawingArea.width;
		int i1 = 0;
		int j1 = tempHeight;
		int k1 = tempWidth;
		int l1 = DrawingArea.width - k1;
		int i2 = 0;
		if (k < DrawingArea.topY) {
			int j2 = DrawingArea.topY - k;
			j1 -= j2;
			k = DrawingArea.topY;
			i1 += j2 * k1;
			l += j2 * DrawingArea.width;
		}
		if (k + j1 > DrawingArea.bottomY) {
			j1 -= (k + j1) - DrawingArea.bottomY;
		}
		if (i < DrawingArea.topX) {
			int k2 = DrawingArea.topX - i;
			k1 -= k2;
			i = DrawingArea.topX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if (i + k1 > DrawingArea.bottomX) {
			int l2 = (i + k1) - DrawingArea.bottomX;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if (!(k1 <= 0 || j1 <= 0)) {
			method349(DrawingArea.pixels, tempArray, i1, l, k1, j1, l1, i2);
		}
	}
	
	
	public void drawSprite2(int i, int j) {
		int k = 225;//was parameter
		i += anInt1442;
		j += anInt1443;
		int i1 = i + j * DrawingArea.width;
		int j1 = 0;
		int k1 = myHeight;
		int l1 = myWidth;
		int i2 = DrawingArea.width - l1;
		int j2 = 0;
		if(j < DrawingArea.topY) {
			int k2 = DrawingArea.topY - j;
			k1 -= k2;
			j = DrawingArea.topY;
			j1 += k2 * l1;
			i1 += k2 * DrawingArea.width;
		}
		if(j + k1 > DrawingArea.bottomY)
			k1 -= (j + k1) - DrawingArea.bottomY;
		if(i < DrawingArea.topX) {
			int l2 = DrawingArea.topX - i;
			l1 -= l2;
			i = DrawingArea.topX;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if(i + l1 > DrawingArea.bottomX) {
			int i3 = (i + l1) - DrawingArea.bottomX;
			l1 -= i3;
			j2 += i3;
			i2 += i3;
		}
		if(!(l1 <= 0 || k1 <= 0)) {
			method351(j1, l1, DrawingArea.pixels, myPixels, j2, k1, i2, k, i1);
		}
	}

	private void method349(int ai[], int ai1[], int j, int k, int l, int i1,
						   int j1, int k1)
	{
		int i;//was parameter
		int l1 = -(l >> 2);
		l = -(l & 3);
		for(int i2 = -i1; i2 < 0; i2++)
		{
			for(int j2 = l1; j2 < 0; j2++)
			{
				i = ai1[j++];
				if(i != 0)
					ai[k++] = i;
				else
					k++;
				i = ai1[j++];
				if(i != 0)
					ai[k++] = i;
				else
					k++;
				i = ai1[j++];
				if(i != 0)
					ai[k++] = i;
				else
					k++;
				i = ai1[j++];
				if(i != 0)
					ai[k++] = i;
				else
					k++;
			}

			for(int k2 = l; k2 < 0; k2++)
			{
				i = ai1[j++];
				if(i != 0)
					ai[k++] = i;
				else
					k++;
			}

			k += j1;
			j += k1;
		}

	}

	private void method351(int i, int j, int ai[], int ai1[], int l, int i1,
						   int j1, int k1, int l1)
	{
		int k;//was parameter
		int j2 = 256 - k1;
		for(int k2 = -i1; k2 < 0; k2++)
		{
			for(int l2 = -j; l2 < 0; l2++)
			{
				k = ai1[i++];
				if(k != 0)
				{
					int i3 = ai[l1];
					ai[l1++] = ((k & 0xff00ff) * k1 + (i3 & 0xff00ff) * j2 & 0xff00ff00) + ((k & 0xff00) * k1 + (i3 & 0xff00) * j2 & 0xff0000) >> 8;
				} else
				{
					l1++;
				}
			}

			l1 += j1;
			i += l;
		}
	}

	public void method352(int i, int j, int ai[], int k, int ai1[], int i1,
						  int j1, int k1, int l1, int i2)
	{
		try
		{
			int j2 = -l1 / 2;
			int k2 = -i / 2;
			int l2 = (int)(Math.sin((double)j / 326.11000000000001D) * 65536D);
			int i3 = (int)(Math.cos((double)j / 326.11000000000001D) * 65536D);
			l2 = l2 * k >> 8;
			i3 = i3 * k >> 8;
			int j3 = (i2 << 16) + (k2 * l2 + j2 * i3);
			int k3 = (i1 << 16) + (k2 * i3 - j2 * l2);
			int l3 = k1 + j1 * DrawingArea.width;
			for(j1 = 0; j1 < i; j1++)
			{
				int i4 = ai1[j1];
				int j4 = l3 + i4;
				int k4 = j3 + i3 * i4;
				int l4 = k3 - l2 * i4;
				for(k1 = -ai[j1]; k1 < 0; k1++)
				{
					int x1 = k4 >> 16;
				int y1 = l4 >> 16;
				int x2 = x1 + 1;
				int y2 = y1 + 1;
				int c1 = myPixels[x1 + y1 * myWidth];
				int c2 = myPixels[x2 + y1 * myWidth];
				int c3 = myPixels[x1 + y2 * myWidth];
				int c4 = myPixels[x2 + y2 * myWidth];
				int u1 = (k4 >> 8) - (x1 << 8);
				int v1 = (l4 >> 8) - (y1 << 8);
				int u2 = (x2 << 8) - (k4 >> 8);
				int v2 = (y2 << 8) - (l4 >> 8);
				int a1 = u2 * v2;
				int a2 = u1 * v2;
				int a3 = u2 * v1;
				int a4 = u1 * v1;
				int r = (c1 >> 16 & 0xff) * a1 + (c2 >> 16 & 0xff) * a2 +
					(c3 >> 16 & 0xff) * a3 + (c4 >> 16 & 0xff) * a4 & 0xff0000;
				int g = (c1 >> 8 & 0xff) * a1 + (c2 >> 8 & 0xff) * a2 +
					(c3 >> 8 & 0xff) * a3 + (c4 >> 8 & 0xff) * a4 >> 8 & 0xff00;
				int b = (c1 & 0xff) * a1 + (c2 & 0xff) * a2 +
					(c3 & 0xff) * a3 + (c4 & 0xff) * a4 >> 16;
				DrawingArea.pixels[j4++] = r | g | b;
					k4 += i3;
					l4 -= l2;
				}

				j3 += l2;
				k3 += i3;
				l3 += DrawingArea.width;
			}

		}
		catch(Exception _ex)
		{
		}
	}

	public void method353(int i,
						  double d, int l1)
	{
		//all of the following were parameters
		int j = 15;
		int k = 20;
		int l = 15;
		int j1 = 256;
		int k1 = 20;
		//all of the previous were parameters
		try
		{
			int i2 = -k / 2;
			int j2 = -k1 / 2;
			int k2 = (int)(Math.sin(d) * 65536D);
			int l2 = (int)(Math.cos(d) * 65536D);
			k2 = k2 * j1 >> 8;
			l2 = l2 * j1 >> 8;
			int i3 = (l << 16) + (j2 * k2 + i2 * l2);
			int j3 = (j << 16) + (j2 * l2 - i2 * k2);
			int k3 = l1 + i * DrawingArea.width;
			for(i = 0; i < k1; i++)
			{
				int l3 = k3;
				int i4 = i3;
				int j4 = j3;
				for(l1 = -k; l1 < 0; l1++)
				{
					int k4 = myPixels[(i4 >> 16) + (j4 >> 16) * myWidth];
					if(k4 != 0)
						DrawingArea.pixels[l3++] = k4;
					else
						l3++;
					i4 += l2;
					j4 -= k2;
				}

				i3 += k2;
				j3 += l2;
				k3 += DrawingArea.width;
			}

		}
		catch(Exception _ex)
		{
		}
	}

	public void method354(Background background, int i, int j)
	{
		j += anInt1442;
		i += anInt1443;
		int k = j + i * DrawingArea.width;
		int l = 0;
		int i1 = myHeight;
		int j1 = myWidth;
		int k1 = DrawingArea.width - j1;
		int l1 = 0;
		if(i < DrawingArea.topY)
		{
			int i2 = DrawingArea.topY - i;
			i1 -= i2;
			i = DrawingArea.topY;
			l += i2 * j1;
			k += i2 * DrawingArea.width;
		}
		if(i + i1 > DrawingArea.bottomY)
			i1 -= (i + i1) - DrawingArea.bottomY;
		if(j < DrawingArea.topX)
		{
			int j2 = DrawingArea.topX - j;
			j1 -= j2;
			j = DrawingArea.topX;
			l += j2;
			k += j2;
			l1 += j2;
			k1 += j2;
		}
		if(j + j1 > DrawingArea.bottomX)
		{
			int k2 = (j + j1) - DrawingArea.bottomX;
			j1 -= k2;
			l1 += k2;
			k1 += k2;
		}
		if(!(j1 <= 0 || i1 <= 0))
		{
			method355(myPixels, j1, background.aByteArray1450, i1, DrawingArea.pixels, 0, k1, k, l1, l);
		}
	}

	private void method355(int ai[], int i, byte abyte0[], int j, int ai1[], int k,
						   int l, int i1, int j1, int k1)
	{
		int l1 = -(i >> 2);
		i = -(i & 3);
		for(int j2 = -j; j2 < 0; j2++)
		{
			for(int k2 = l1; k2 < 0; k2++)
			{
				k = ai[k1++];
				if(k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if(k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if(k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if(k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
			}

			for(int l2 = i; l2 < 0; l2++)
			{
				k = ai[k1++];
				if(k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
			}

			i1 += l;
			k1 += j1;
		}

	}
	
	public static Sprite getError() {
		return new Sprite(errorMessage, 249, 199);
	}

	public int myPixels[];
	public int myWidth;
	public int myHeight;
	private int anInt1442;
	int anInt1443;
	public int anInt1444;
	public int anInt1445;
	
	private final byte[] EMPTY = {-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 
				72, 68, 82, 0, 0, 0, 27, 0, 0, 0, 27, 8, 6, 0, 0, 0, -115,
				-44, -12, 85, 0, 0, 0, 1, 115, 82, 71, 66, 0, -82, -50, 28,
				-23, 0, 0, 0, 4, 103, 65, 77, 65, 0, 0, -79, -113, 11, -4, 
				97, 5, 0, 0, 0, 32, 99, 72, 82, 77, 0, 0, 122, 38, 0, 0, 
				-128, -124, 0, 0, -6, 0, 0, 0, -128, -24, 0, 0, 117, 48, 
				0, 0, -22, 96, 0, 0, 58, -104, 0, 0, 23, 112, -100, -70, 
				81, 60, 0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 14, -60, 0, 
				0, 14, -60, 1, -107, 43, 14, 27, 0, 0, 2, 2, 73, 68, 65, 
				84, 72, 75, -91, 86, -63, 74, -61, 64, 16, 77, 82, -102, 
				-76, 32, -6, 1, -118, 39, 15, 69, -20, 73, 111, 122, -13, 
				119, 61, -118, 55, 111, -98, -84, -126, -46, -117, 7, 65,
				16, 127, 64, 17, 108, 90, -38, -24, -37, -8, -30, 100, 50,
				-69, -37, -30, 66, -39, 116, 54, 51, 111, -34, -101, -39,
				-35, -92, 85, 82, 85, 73, 100, 28, -113, -9, 99, -81, 36,
				-9, -45, -41, -24, 59, 105, 8, 108, 60, -38, -13, 6, -56,
				-5, -103, 91, -101, 47, 86, 110, -58, -1, 94, -42, 75, 110,
				31, 95, -68, 62, 38, 88, 8, -124, -127, 49, 35, -8, 114, -75,
				116, 51, 71, -34, -17, -3, 36, -80, 52, 65, -21, -12, -60,
				-112, 64, -56, -106, 12, -84, 116, 1, 36, 7, -128, 56, -50,
				78, 14, 58, 46, 45, 48, 13, 4, -119, 40, -109, -12, -44, 54,
				11, 20, -20, 52, 96, 35, -93, 5, -28, 19, 95, -77, -123, -116,
				-110, 85, 81, -28, -50, -75, 44, -25, 110, -66, -71, 123, 118,
				-77, 99, -10, 95, 32, -103, 20, -127, 96, -29, 51, 25, -74, 100,
				12, -43, 7, -50, 62, 70, -110, -107, 79, 13, -40, -45, -93, -47,
				110, 116, -97, -123, -128, 116, 112, -55, -116, 107, -112, 19, 118,
				-57, 76, 102, 108, -79, 11, -43, 8, 65, -28, -113, 0, -61, 65, -98,
				-32, 39, -27, 116, 96, -24, 46, -67, 73, -103, -124, 13, 94, -73,
				-72, -59, 2, 118, -126, -24, -25, -50, 62, 99, 102, 22, 8, -70,
				110, -8, -37, 105, -21, 0, 73, -106, 77, 55, -110, 93, -88, -72,
				-78, -67, 53, 16, 37, -109, -116, 16, 107, 80, -12, -35, -113,
				-93, 97, -58, -45, -62, 58, 53, -12, 62, -78, -22, 18, 74, 20,
				-128, 72, -92, -45, 32, -38, -55, -73, 97, 99, -63, 37, 35, -78,
				-12, -42, 12, 47, -124, -128, -76, 100, 33, 112, -84, -51, -54,
				69, -51, -116, -127, -27, -23, -83, -99, 101, -99, 66, 64, 100,
				-108, 102, 69, 43, 4, -20, 13, -104, 62, 76, 37, -80, -81, -13,
				116, 66, 90, 58, -67, -34, -110, 49, 116, 101, 108, 90, -93, 106,
				85, 118, 92, -78, -23, -45, 91, -57, -72, 9, -85, 16, -101, -19, 
				-83, -99, 38, -10, -59, -43, -28, 79, 70, 89, 59, 60, -29, 112, 
				93, 87, 62, 31, -21, -113, -49, -9, -42, 82, 115, -97, -15, -93, 
				-122, 29, 104, 1, -55, -58, -120, -43, 71, -94, -128, 21, 70, 
				-21, 27, 4, -128, -42, 113, 100, -99, 12, -79, 86, 103, -69, 
				95, 94, 63, 52, -81, -74, 26, -124, -97, 99, 96, -59, 91, 118, 
				-45, -3, -60, -56, -40, 87, 18, 8, -10, -50, -90, -26, 21, 78, 
				25, -65, 102, -11, -43, -50, -79, -114, 124, 22, 80, 71, 70, 
				45, -51, -7, -23, -95, 51, -7, -40, 89, -64, -84, -113, 37, 
				115, -16, 35, -107, 14, 33, 80, 0, -6, -104, 104, -64, 111, 
				-60, 87, -104, -24, 65, 38, 115, 44, 0, 0, 0, 0, 73, 69, 78, 
				68, -82, 66, 96, -126};
				
	private static final byte[] HITPOINTS = {-119, 80, 78, 71, 13, 10, 26, 10, 0,
						0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 56, 0, 0, 0, 7, 8, 6, 0,
						0, 0, 93, -86, -114, 102, 0, 0, 0, 4, 103, 65, 77, 65, 0, 0,
						-79, -113, 11, -4, 97, 5, 0, 0, 1, 121, 73, 68, 65, 84, 56,
						79, -35, -108, -53, 43, -124, 81, 24, -121, -33, 47, -111,
						-107, 13, 54, 44, 40, 74, -92, 44, -58, 102, 74, 86, 108, 44,
						101, -93, -79, 16, 43, -39, -38, 88, 88, -79, -103, -115,
						-110, -90, -122, 70,-115, 68, -109, 88, -111, 68, -55, 37,
						-105, -56, -62, 2, -111, -1, -27, -25, -3, 57, -34, 111, 
						-50, 124, -58, 92, -54, -54, 87, 79, -17, -71, 125, -25, 
						-100, -25, -36, 2, -24, 23, -12, 4, -14, 31, 63, -68, 64,
						-75, -70, 5, 100, -26, 68, 48, 126, 47, 72, 92, -28, -103,
						58, 21, 84, -62, -28, -95, -128, 76, 28, 56, 70, -9, 11,
						25, -39, 22, 68, 25, -42, 50, 18, -49, 10, 98, -103, 60,
						29, 41, 65, -108, -42, 85, 65, -75, -48, 105, -96, -66, 
						70, 13, 53, -79, -8, -24, -40, 122, 22,-28, 62, -54, -109,
						125, 23, 24, 27, 111, 2, 35, -3, 42, 72, 62, -3, -60, -6, -9,
						-29, -126, 46, 102, 41, -40, -74, 88, -3, -20, -107, 32, 74,
						-30, 76, 55, -59, -93, 97, 93, 64, 66, -63, -91, 27, 39, -73,
						-87, -99, 102, 30, 28, 76, -5, 121, 43, 103, 76, -35, 9, -8,
						15, 99, -14, -42, -91, -25, -81, 5, 115, 58, 56, -79, 9, 76,
						95, 10, 12, -1, 100, 48, 61, 118, 94, -56, -112, -98, 22, -97,
						-63, 99, 1, -119, 31, 9, -6, -11, 116, -112, 94, 61, 29, -107,
						-64, -74, -75, -53, 17, 65, -82, -104, 73, -3, 38, 102, -110,
						-108, 42, 38, 99, 98, 86, 71, 57, 19, -15, 5, 125, 57, -109,
						50, -95, 104, -84, 70, -80, 115, 79, -48, -68, 43, -88, 91,
						113, -124, 59, 40, 93, 110, -62, -100, 16, 7, -120, -23, 125,
						52, -6, 52, 95, -55, -86, -79, 115, -46, -106, 83, 118, 4,
						-19, 74, -53, 90, 105, 26, -45, 2, -125, 119, -84, 73, -17,
						95, -79, -69, -58, 114, -65, -83, 29, 65, 19, 97, -28, -114,
						25, -52, -121, 119, -112, -81, -24, -41, 67, -93, -110, 33,
						-47, -68, 95, 87, 109, -70, 92, 95, 86, -1, -3, -40, -39,
						-93, -9, 23, -111, 110, -97, -128, 46, 123, -112, -108, -7,
						117, -5, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};
						
	private static final byte[] errorMessage = {
		-119, 80, 78, 71, 13, 10, 26, 10, 0, 0,
		13, 73, 72, 68, 82, 0, 0, 0, -7, 0,
		0, -57, 8, 6, 0, 0, 0, -37, 63, -11,
		0, 0, 0, 1, 115, 82, 71, 66, 0, -82,
		28, -23, 0, 0, 0, 4, 103, 65, 77, 65,
		0, -79, -113, 11, -4, 97, 5, 0, 0, 0,
		112, 72, 89, 115, 0, 0, 14, -60, 0, 0,
		-60, 1, -107, 43, 14, 27, 0, 0, 99, 33,
		68, 65, 84, 120, 94, -19, -67, 9, -112, 100,
		117, 29, -102, -67, 85, -17, -37, -52, -12, 76,
		102, -63, 12, 48, 59, -74, 1, 8, 18, 2,
		-127, 32, 1, -112, 4, -59, 13, -76, 36, -110,
		67, 34, 28, -46, 55, -91, -80, -4, -3, -65,
		67, -78, -62, -1, 75, -74, 35, 100, 69, -40,
		-41, -1, -95, 111, -121, 44, -39, -108, 35, 68,
		-28, 111, 6, 41, 19, 38, 37, 2, -92, 72,
		32, 9, 113, -63, 96, -97, 21, -77, 99, -70,
		-90, -9, -67, -70, -70, -37, -9, -36, -84, 91,
		47, 43, -33, 123, -7, 94, -67, -22, 101, -90,
		-16, -48, 85, -11, -14, -27, -53, -27, -98, -68,
		-17, -67, 121, -77, 97, -5, -74, 45, 43, -121,
		-19, 87, 72, -19, 29, -19, -4, 23, -87, -91,
		69, 45, 46, 45, -86, -74, -42, 54, 85, 40,
		-7, -81, -92, -23, -39, 89, -43, -37, -45, -85,
		103, 102, 85, 71, 103, -121, 90, 42, 44, 42,
		102, -26, -103, 95, -104, -25, -20, -8, -83, -75,
		-107, -53, -66, 112, -15, -110, -54, -75, 52, -85,
		-106, 38, -75, -72, -72, -60, -9, -112, 79, -98,
		103, -16, -36, -35, 119, -33, -83, 70, 71, 71,
		-87, -109, -89, -44, -56, -11, 81, -43, -35, -45,
		114, -71, -100, -54, -25, -13, -4, 87, -22, 58,
		59, -57, -65, 53, 52, 54, -87, -106, -26, 38,
		-67, -85, -77, 91, 21, -88, -18, -115, 13, -51,
		121, -91, -64, -11, 88, 94, 92, 86, -115, 45,
		-4, 121, 126, -98, -34, -39, -90, -37, -45, 76,
		108, 106, 106, 84, 75, 75, -53, 106, 121, 121,
		-21, -74, -112, 95, 80, 121, -86, 95, 55, -38,
		-65, 115, 90, 89, 82, -71, 86, 124, 95, 44,
		3, -14, -39, 9, -27, -31, -35, -26, -5, 80,
		-114, -54, 117, -91, -42, 92, -85, -102, -104, -100,
		-35, 66, -67, -16, 14, -12, 15, -9, 31, 125,
		110, 108, 84, -123, -27, 101, -82, -73, -4, 118,
		-64, -127, 122, -1, -84, -61, -2, -103, -99, -101,
		-113, 101, 17, 51, 66, -29, -8, 43, -76, -97,
		44, -88, -35, -69, 118, 42, -48, -17, -62, -62,
		-29, 64, -16, 34, 5, -32, -73, -82, -114, 14,
		-44, -36, 82, -62, 26, 104, 5, -65, -103, 121,
		-101, 115, -4, -68, 96, 86, -18, -95, 108, -92,
		-18, -82, -114, -33, -35, -78, 101, 51, 127, -23,
		-22, 86, -51, -51, -51, 68, -92, 5, 69, 116,
		0, 89, -31, -49, -8, -101, 95, 92, -28, 107,
		-103, 8, -106, 8, 115, 105, 105, -119, -66, -25,
		-126, -8, 93, -64, -116, -25, 23, 22, -26, -44,
		126, -111, -63, -77, -68, -46, 64, -60, 57, -85,
		8, -124, 5, -102, 12, -112, 47, 79, -9, -112,
		-119, 104, -111, 7, -49, -24, 6, -26, -24, -67,
		-4, 125, 122, 122, -102, 58, 0, 21, 111, 86,
		-51, -115, -108, 79, 3, -72, -103, -66, -73, -74,
		-14, 36, -125, -78, 90, 114, 45, 106, -123, -98,
		-23, -22, 80, -99, 93, 93, -4, -20, -14, -54,
		-125, 119, 113, -127, -64, -33, -44, -64, 101, -73,
		-60, -48, -46, -46, -62, -11, 70, 30, -36, -57,
		-106, 22, -3, -50, 38, 42, 23, 109, 107, 108,
		80, 43, -53, 13, -36, -2, -10, -74, 118, -75,
		109, -57, 111, -117, 5, -12, -119, 46, 11, -64,
		-17, 74, -23, 122, -75, -75, 117, 112, 57, -83,
		-1, 102, 61, 49, 52, -82, -88, -114, -10, 46,
		102, 108, 116, 68, 53, 52, -48, -77, 84, -81,
		-67, 115, 118, 110, -82, -44, -26, -103, -103, 25,
		114, -127, -54, 7, 1, -76, 83, 89, -24, 111,
		35, 93, 0, 60, -18, 53, -46, 123, 26, 26,
		-72, -65, 80, -33, -23, -87, 122, -1, -84, -49,
		-47, -12, 12, 90, -58, 56, 34, 1, 67, 66,
		96, 36, -96, -37, 21, -94, 5, 96, 2, -96,
		70, -16, 29, -64, -89, -31, 85, -19, -19, 29,
		70, 3, -3, -101, -99, -41, -109, 0, 104, 26,
		-15, 123, 126, -111, 38, 5, 98, 108, -116, -57,
		70, -15, 30, -63, 44, -54, 71, 62, 97, 10,
		-112, -73, -73, 107, 78, -114, 74, 117, 119, -9,
		123, -113, -34, -89, 30, -2, -55, 71, -44, -48,
		43, 4, -128, 101, -43, -41, -41, 71, 4, -73,
		-100, 89, -125, 121, -127, 43, -116, 6, 1, 68,
		0, 2, 50, -79, 85, -43, -54, -33, -105, 25,
		122, 48, 52, -72, -112, -16, -73, -77, -77, 83,
		-46, -116, 36, 21, -62, 125, -108, 33, -60, -115,
		55, 82, -117, 1, -46, -66, -34, 62, -43, 69,
		6, 80, 114, -12, -82, 54, -54, 7, -48, -31,
		52, 36, 63, -65, 64, -77, -99, 6, 69, 57,
		29, -38, 114, 12, -40, 21, -86, -121, -18, 36,
		16, -44, 73, 0, 11, 0, 45, -28, -25, 25,
		-32, -64, 40, 19, -7, -111, 24, -104, -59, -60,
		-89, -119, 64, 119, 38, 38, 1, -86, 11, 77,
		-124, 92, -102, 69, -25, -88, 62, 52, -95, 80,
		102, -110, 38, 48, 97, -32, 126, 87, 103, -69,
		-94, -55, 106, -119, -6, 12, 109, -103, 33, -55,
		15, -69, -94, -74, 119, -14, -69, 4, -8, 24,
		-108, -121, 119, -16, -17, -108, -48, 54, -4, -122,
		-57, 111, 57, -102, -47, 117, -33, 106, -18, 95,
		-97, -11, -41, 63, -126, 13, -48, 25, -58, 12,
		65, -29, 66, -21, 0, -86, 96, 64, 48, -127,
		27, 88, 1, 83, -63, 115, -96, 88, 97, 118,
		52, -71, -29, 51, 48, 9, -52, -128, -58, 49,
		0, -117, -126, -55, -113, 126, -20, -29, 68, 107,
		106, 98, 98, -110, 49, 41, 56, 16, -112, 55,
		-30, -6, -64, -64, 86, -75, 115, -41, 14, -30,
		5, 117, -32, -64, 126, 117, -110, 68, -27, -18,
		14, 53, 53, 53, 75, 5, 22, -104, -29, -128,
		-75, -73, 117, -86, -71, -7, 25, 42, 48, 95,
		65, 51, 113, -37, 66, 65, -125, -93, 64, -94,
		0, 35, 34, 49, 126, -61, 115, 72, -26, 51,
		-81, -117, -72, 48, 63, 71, -49, -125, -101, -95,
		-59, 73, -112, 68, 112, 0, -102, 102, -83, -94,
		46, 47, -52, -75, -76, -14, -116, -122, -33, -25,
		12, -19, -32, -122, -12, -34, -26, -94, 88, 12,
		-66, 12, 80, 112, -64, 50, 96, 81, -26, 66,
		-106, -71, 46, -98, -57, 115, 109, -19, 36, 102,
		-21, 95, 122, -48, -8, 32, 101, -77, -92, 67,
		3, 18, -34, -127, 126, 49, -97, 107, 98, -15,
		-127, -71, -5, -28, -28, -72, 26, 27, 31, 15,
		-121, 62, 65, 93, 37, -95, 92, 36, -52, -56,
		92, 121, -110, -110, -91, -119, -7, 48, -6, 88,
		44, -19, -85, -9, 79, -71, -121, -42, 67, -1,
		-50, 33, 17, 98, -71, -123, -6, 32, 77, 79,
		-78, -28, 40, -76, 25, -122, 7, -128, -46, -52,
		98, 74, -98, 49, -79, -121, -4, -96, 7, -63,
		96, 22, 116, 116, -23, -30, 101, 117, -19, -38,
		126, 63, -125, 124, -1, -2, -37, -7, -53, -64,
		109, 36, 6, 119, -15, 103, 19, -96, 37, -126,
		-38, -105, 56, -119, 106, 36, 81, -93, -119, 56,
		17, -85, -71, 78, 4, -63, 110, -39, -78, -123,
		15, 13, 95, 113, 86, 88, -14, -101, -60, 107,
		35, -65, 11, -57, 107, -94, 70, -96, -46, 0,
		18, -127, 43, -105, -45, 0, 89, 34, 112, -31,
		-4, 109, 36, 46, 10, 29, -126, -28, -111, -17,
		-89, 72, -25, -94, -116, -80, -124, 124, -32, -56,
		8, 124, 19, -98, 1, 56, 81, 7, 51, 73,
		-48, -42, 121, 90, 23, -75, -47, 50, 67, -38,
		-39, -47, -59, -7, 1, 116, -44, 71, -14, -30,
		-13, 55, -76, 89, 38, -106, 69, -102, -59, -79,
		-127, -24, -114, -11, -67, -28, -61, 51, -11, -2,
		-3, -50, 58, -22, -97, -96, 52, -87, 113, 20,
		-13, 46, -58, 56, -72, 109, 59, -109, -46, -11,
		-41, -103, 110, 77, 124, -31, -13, -46, 18, 49,
		-27, 70, 22, -17, -101, 13, 70, 38, -12, 39,
		39, 38, -89, -43, -75, -31, -31, 50, -56, 69,
		-42, -33, -33, 95, -94, -43, -125, -121, 14, -88,
		125, -15, -53, -91, -17, 16, 21, 100, 50, 120,
		-11, 55, 3, 68, 13, -123, 87, 79, 111, -113,
		25, 25, 35, 73, -32, 22, 22, 79, -122, -82,
		-87, 105, 18, 79, 37, -35, 113, -57, 97, 117,
		-32, 62, -80, 44, 117, -20, -40, -85, -22, -20,
		-13, 124, -85, -117, -108, 91, 125, -101, 72, 28,
		101, 25, -60, 22, -120, 27, 16, 111, -49, -98,
		-25, -117, -75, 122, -66, 122, 15, -84, -101, 30,
		-69, 119, 15, 49, -118, 38, 22, -45, -95, -101,
		-98, -103, 82, -29, -93, -29, 37, 44, -20, -35,
		-85, 58, 122, -12, 46, 98, -108, 13, -22, -28,
		-45, -54, -60, 18, -80, 48, -72, 125, -112, -107,
		-105, 46, -66, -83, 54, 111, -18, 87, -109, 36,
		47, 18, 30, -52, 4, 44, 33, -99, 58, 117,
		-106, -128, 51, -91, 91, 31, 127, -22, 35, -22,
		-15, -109, -91, -17, 99, 99, 99, -107, 32, -33,
		101, -128, -108, -56, -102, 43, -19, -37, -73, 95,
		-7, -53, -49, -16, -25, 101, -86, 108, 119, 79,
		-70, -5, -98, 59, -43, 105, 42, 120, 120, -24,
		-81, 7, 37, 117, -76, -73, -87, 91, -87, -14,
		-65, -14, -102, -22, -89, -75, -62, -42, 109, 91,
		-103, 83, 103, 73, 35, 28, -84, -36, 61, 119,
		-55, -115, 120, -15, 123, 47, 114, -59, 49, 33,
		14, 14, -106, -92, 6, 76, 12, 23, 46, 92,
		101, -22, 18, 43, -76, 92, 73, -60, 120, -71,
		-6, -84, 34, -37, 55, -106, -48, 53, 27, -12,
		-86, 87, -95, -72, 102, -114, 123, -119, -85, 94,
		-98, -113, 43, 127, 61, -36, -81, -113, 71, 112,
		-80, -50, -122, -91, 103, -9, -18, -99, 76, -21,
		-32, -82, 67, 67, 67, 12, 92, 48, -60, 7,
		124, -128, -103, 32, -16, 98, -90, 102, 122, -18,
		-3, 123, -43, -43, -31, -21, -68, -52, 3, 94,
		19, 51, -100, -99, 43, 75, -96, -64, -30, -74,
		-83, 106, 31, 73, -33, -81, -68, -4, -102, -102,
		-100, 42, 97, -15, 35, 31, -7, -80, 58, 125,
		-108, 6, 54, 73, 126, -93, -41, -81, -15, 103,
		-68, 117, -109, 82, 11, -36, 19, -30, 67, 11,
		-2, 36, 10, -128, 51, 3, -48, -4, 0, -76,
		-92, 24, 66, -66, -13, -25, 47, -78, 120, 4,
		2, 126, -25, 9, -96, -101, -76, -38, 36, 94,
		-13, 38, -30, -54, -77, 36, -94, -50, -111, 6,
		121, 26, 73, -68, -105, 107, 124, 108, -126, -18,
		45, -65, -96, 122, 122, 122, -120, -77, 31, 96,
		-35, 36, 85, -12, -91, 31, -65, -92, -50, 19,
		-47, 33, 16, 83, 49, -85, 53, -110, -70, 80,
		-108, 38, -88, 109, -30, -122, -24, 2, 5, -104,
		-54, 64, 120, -75, 2, -68, -85, 78, 97, 96,
		29, -20, 122, -72, -98, 7, -16, -19, 118, 102,
		-32, 44, 38, 65, -44, -79, 62, 30, -15, -93,
		53, 53, -61, -42, -109, -31, -31, -85, 106, -28,
		53, 86, -112, -11, -48, 18, -72, -89, -73, -105,
		-100, -72, 50, 45, 121, -89, -90, -90, -44, -59,
		-105, -103, 59, -103, -8, 0, -29, 108, 33, -27,
		-16, 4, 13, 45, 20, -57, 5, -46, -72, -49,
		16, -106, -118, 88, 3, -29, 92, 32, 69, 51,
		-8, -10, -37, 87, -8, 119, 73, -73, -17, -37,
		-58, 105, 114, 0, -64, -25, 102, -89, -43, 12,
		5, -72, -26, 53, -7, -34, 61, -69, 57, 31,
		83, 34, -78, -17, -36, -71, 75, 61, -5, -20,
		-22, -16, -31, -125, 124, -17, -51, 55, 79, 16,
		-18, -27, 60, 72, 16, 69, -38, -24, 51, -76,
		81, 9, -49, 1, -4, -110, -6, 104, -14, -128,
		16, 0, -121, 73, -20, -8, 27, -57, 89, -52,
		-45, 102, -102, 44, -112, 92, 92, -45, 4, -79,
		-64, 89, 16, 116, -4, 80, -90, -49, -79, 22,
		-36, -28, -74, 105, 39, 63, -44, -69, 62, 30,
		-29, 62, 49, 49, 85, 73, -53, 36, 118, 31,
		114, -120, 109, -30, 39, 79, -100, 100, 11, -54,
		-119, -31, -110, 0, 110, -63, 89, -40, 27, 96,
		-98, -121, 21, -87, 40, 65, 3, -24, 99, -29,
		1, 124, 62, -15, -60, -29, -22, -46, -91, -117,
		4, 68, 117, -28, 65, 98, 78, 14, 113, -101,
		-3, 4, 90, -104, -47, -128, 126, -120, -47, 103,
		-97, 81, -101, -73, 108, 34, 37, -64, 8, -49,
		104, 64, -127, 56, 58, -14, 117, -110, 70, 28,
		-100, 21, 90, 99, -49, -110, 35, -52, -103, 51,
		73, -52, -72, -90, -82, 93, -67, -50, -41, 50,
		-41, 69, 51, -46, -64, -64, 22, -2, 46, 9,
		4, 72, 9, 75, 52, 35, -99, 127, -21, 60,
		-90, -20, 8, 98, 54, 48, 71, -10, 111, -109,
		8, -80, 109, -128, 11, -73, -76, -71, 119, 90,
		-114, 31, -58, -22, 114, -44, -110, 99, -5, -44,
		109, -65, -96, -34, -11, -15, -120, -17, 97, -45,
		36, -71, -25, 72, -36, -98, 34, 80, -117, -65,
		-92, 92, -15, 21, 65, -98, 35, 119, 28, -30,
		16, -31, 33, 41, 11, -122, 70, 70, 70, 9,
		112, 0, 115, -74, 77, -5, 25, 26, -45, -115,
    61, -4, -56, -89, 24, -32, 7, 119, 111, -87,
    13, 40, 75, 56, 121, 86, 101, -82, 117, -65,
    -91, 9, -13, 57, -95, 53, -109, -18, -16, 27,
    -46, 78, 46, -128, 71, 113, 111, -5, 121, 96,
    112, 36, -9, -32, -90, -118, -33, -127, 57, 96,
    24, 4, 22, -127, 73, 96, 19, 24, 69, 2,
    -127, 93, -32, 11, 88, 22, 92, 55, 117, 119,
    -4, 110, 119, -73, -106, -23, 59, -69, 58, -39,
    -114, 2, -5, -6, -5, -44, -87, -109, -89, 41,
    -60, -76, -70, 50, 52, 76, 113, -67, -102, 72,
    -33, -83, 118, -19, -36, 77, -54, -71, 25, 90,
    -52, -88, 94, -30, -14, 67, -61, 87, -87, -16,
    -38, 1, -93, -75, -17, 40, -93, 15, -63, 31,
    11, 15, -108, 7, 99, -93, 65, 113, 70, 42,
    49, -60, 56, 104, 99, -93, 90, -58, 65, -123,
    -39, 116, -18, -105, 124, 15, -35, -75, 71, 125,
    -59, 99, -28, -101, 14, 15, -72, -54, 11, 62,
    -8, 125, -111, -20, -106, 88, 71, -91, -71, 92,
    -58, 54, -46, 111, -3, -76, 97, 39, -85, -6,
    44, -71, -78, 42, 115, 45, -53, 73, 67, 15,
    12, -74, 32, -125, -74, -32, -111, 110, -45, 30,
    -40, -96, -67, -57, 126, -30, 30, -11, -59, -25,
    84, 59, 110, -47, 82, -108, -23, -69, 14, -18,
    123, 42, 92, -30, -71, 9, 112, -60, 102, -125,
    109, 43, 73, -47, -64, 19, 18, -10, -121, 99,
    -115, 0, -92, 123, -9, -34, -54, -109, 120, 35,
    3, 6, 33, 73, 95, -68, 112, 89, -99, 59,
    -127, 1, 13, -68, 29, 56, -80, 79, 77, 19,
    -95, 3, 67, 125, -79, 73, -91, -60, -55, -71,
    116, 1, -32, 91, 54, 19, 71, -96, 13, 31,
    77, 122, -9, -115, 68, 56, -59, 58, 2, -66,
    5, 10, 42, 1, -9, -70, -85, 100, -97, -125,
    28, 54, -80, 108, 37, -69, 30, 22, -6, -20,
    67, 127, -49, -66, 117, -98, -107, 6, 103, 78,
    46, -75, 3, 54, 63, -120, 36, 16, -43, 69,
    49, 99, -71, -37, -63, 33, -19, -96, -128, -24,
    -71, -14, -76, 62, -105, 75, 76, 27, -111, -94,
    -3, 102, -67, 7, 82, -12, -128, -48, -106, 73,
    38, 29, 70, 21, 105, 110, 54, 49, 1, 14,
    7, 6, -128, 5, 96, 66, 18, -80, 2, -52,
    59, 38, -106, -128, 45, 96, 12, 88, 3, -26,
    61, 96, 16, 88, 20, 13, -67, 96, 20, -104,
    118, -127, 97, 96, 89, 112, -51, -30, 122, 59,
    3, -72, -112, 0, 60, 28, 127, 90, 88, 10,
    -33, -60, 98, 31, 9, 51, 5, -30, -67, 93,
    -66, 70, -21, -127, 89, 114, 123, -19, 86, -69,
    -77, -118, -40, -20, 68, -84, -8, -12, -89, 63,
    112, -67, -9, 125, -17, 97, 80, 95, 29, -42,
    -19, -40, -16, 98, 38, 27, -24, 16, 49, 68,
    105, 6, -28, -105, 14, -57, 95, -20, -17, -75,
    90, -117, -123, -11, -9, 111, -36, 37, 79, -40,
    -39, 52, 6, -70, 51, -23, -48, 5, 114, -48,
    -120, -25, -116, 39, 107, 39, -103, -119, 1, 96,
    -40, 0, 70, 4, 47, -78, 12, 70, 62, 96,
    -40, 2, -58, -128, 53, 96, 14, -40, 3, 6,
    4, -109, 82, 15, 96, 22, -40, 21, -85, -106,
    58, -80, 38, -25, 45, 108, -60, -30, -7, -80,
    18, 19, -112, 76, 63, -11, -21, -41, 70, -56,
    87, 14, 1, -123, -5, -48, -30, 97, 86, -127,
    -127, -108, -93, -115, -20, -16, -103, -3, -47, 15,
    72, -74, -68, -21, 44, -46, 15, 108, 27, 80,
    -76, -25, 85, 118, -72, 97, 77, 17, -44, 92,
    -45, 89, 100, 7, 14, 102, 40, 108, -53, -107,
    -65, 115, -13, -120, 78, 89, 54, -115, -39, -33,
    15, -30, 124, -3, -86, -9, 65, -106, 52, 96,
    -8, 92, 116, 104, 3, 93, -72, -85, -48, -77,
    93, 20, 122, 7, -19, 35, 1, 11, -64, 4,
    1, -116, 0, 43, -64, 12, -80, 3, 12, 33,
    83, -64, -106, 109, -11, 2, 6, -127, 69, 73,
    81, 96, 86, 99, -105, -84, 80, -124, 101, 89,
    107, 59, 121, 17, 96, -112, -19, -95, -90, 103,
    -92, -72, 49, 123, 59, 45, -16, -111, -80, -55,
    -38, -75, -85, -91, 40, 20, 80, 14, 96, 111,
    -44, -2, 5, 82, -124, 65, -69, -121, -44, 65,
    123, -52, 54, 112, -71, -61, -34, -42, 97, 90,
    75, -62, -98, 87, 104, 3, -79, -23, 93, 26,
    123, -24, 0, 81, -6, 65, 83, -104, -43, 113,
    -10, 0, -44, -65, -41, 123, -96, -42, 61, 0,
    101, 26, 46, 42, -65, -52, -109, -121, 64, -13,
    125, 96, 0, 88, -112, 4, -116, 0, 43, -64,
    -80, 3, 12, 33, 1, 83, -64, 22, 48, 6,
    1, 115, 72, -120, 4, 3, 44, -54, -58, 47,
    40, 48, 11, -20, 2, -61, -116, -89, 34, -82,
    -124, 118, -53, 118, -83, 52, -128, -71, 12, -31,
    97, -128, 63, 72, -111, 97, -66, -14, 63, -66,
    14, 28, -36, -57, -34, 53, -104, 85, -80, -26,
    -42, 81, -28, -125, -52, -113, 96, 113, 72, 115,
    115, -20, -119, 99, 54, 72, 84, -4, -72, -1,
    107, 111, -106, 26, -60, -50, 52, -59, -25, 71,
    -78, -29, -59, -55, -63, -18, -4, -34, -34, 110,
    9, 33, -103, -49, 14, 93, 83, 75, 83, -41,
    83, 119, 84, -56, 29, 93, 2, -108, 38, -110,
    34, 95, -6, -68, 48, 111, 7, -125, 35, 107,
    68, -104, 40, -58, 1, 91, -92, -91, 64, 75,
    41, 80, -1, -84, -105, 68, 27, -67, 31, 84,
    126, 95, 43, 111, -72, 10, -18, 98, 108, 109,
    127, 111, 50, -18, 65, 25, -116, 20, -74, -23,
    -23, -84, 72, -113, 123, 7, 7, 2, -127, 28,
    38, -126, 30, -93, 66, -128, 56, -54, 104, 51,
    84, 3, -121, 5, 120, 33, 126, 75, -70, -29,
    -61, -4, -47, 52, 81, -29, 59, 56, -12, 46,
    93, 111, 111, -45, -101, -58, 16, 100, 85, -98,
    -128, -127, 49, 40, 0, -31, -7, 118, -14, -60,
    -11, -95, -97, 122, 82, -99, -96, -56, 48, 8,
    2, -51, -69, 48, -34, 64, -36, 117, -52, 22,
    57, 18, 34, -61, 60, -5, -20, 115, 37, -1,
    40, 5, 32, 54, 32, 15, 55, -110, 10, -25,
    45, 69, 111, -101, 82, -115, -83, 15, 120, -50,
    63, -97, -83, 70, 51, -45, -127, -125, 7, 120,
    56, -2, -58, -15, 64, 112, 72, 121, 92, 64,
    5, -36, 15, 126, -84, -9, -56, -42, 83, -67,
    -42, 107, 15, 32, -18, 58, 104, 117, -110, 68,
    59, 1, -116, -121, -114, 28, 98, -128, -98, 36,
    54, 56, -78, -104, -52, 13, 96, 22, 59, 121,
    -5, -64, -3, -31, 20, 35, -53, 98, 112, -22,
    -118, -90, 108, -38, -41, -97, 120, -30, 113, -106,
    -112, 0, 114, -119, 30, 19, 0, 57, -62, -56,
    -45, 9, 42, -40, 43, -69, 103, -17, 30, -11,
    -81, -4, 77, -32, -99, 40, 16, -96, -123, 54,
    103, 48, -95, 114, 0, 48, 26, -127, 103, -95,
    -40, 68, 127, -75, 23, -100, 86, 42, -104, 9,
    -59, 1, -102, -51, 48, -61, 116, -46, -28, 112,
    93, 119, -80, 27, -34, -8, -8, 36, 113, -5,
    57, 86, 21, 18, -97, -17, 84, -28, -28, 118,
    93, -57, 38, -71, 58, 70, -50, -59, -118, 34,
    -45, -87, 70, 107, -13, 105, -49, 58, -97, -83,
    29, 41, -7, -44, 35, -18, 109, -82, 3, 6,
    99, 123, -19, -6, 74, 91, -20, 114, 93, -121,
    34, -113, 121, 36, -111, -7, 93, -98, -113, -22,
    -6, 120, -24, 94, -110, 62, 2, 39, 47, -19,
    35, -65, -14, 59, -18, -68, -125, 76, 97, 96,
    -53, -22, -75, 87, 95, 103, 119, 84, 72, -78,
    -71, -19, -115, 91, -64, 19, -108, 109, 96, -98,
    20, 114, 109, -21, -74, 45, 28, 122, 13, 18,
    96, 13, 101, -25, -118, -38, 121, -37, -47, -20,
    15, 125, 64, -99, 59, 123, -114, -29, 46, -52,
    9, 42, 18, -74, 45, 112, 62, -71, 0, -100,
    86, 20, -59, -15, 25, 96, 21, -37, 57, 42,
    0, -57, 61, -68, 28, 11, 125, 14, 55, 51,
    -50, -107, -21, -94, 0, -17, 114, -30, 34, 42,
    23, 68, 127, -124, 119, 70, 66, 76, -9, 115,
    -50, -15, 102, -9, 46, 90, -125, 32, -68, -51,
    119, -33, 73, -46, 66, 95, 105, -19, -31, 34,
    -13, 64, -69, 40, 112, -56, -127, 119, 81, 121,
    123, 41, 12, 20, 113, -32, -117, -69, -17, 2,
    -36, 51, 97, 109, 14, 123, -50, 5, 112, 77,
    -63, 99, -18, -20, -17, 82, -98, 89, 71, -45,
    25, 118, 58, -87, 93, -113, -6, 120, 4, 123,
    -53, 81, -48, 48, 104, 25, 52, 13, -38, 6,
    -125, -42, 65, -13, 72, -64, 0, -80, 96, 98,
    78, 14, 6, 118, -128, 33, 96, 9, -104, 2,
    68, -79, -122, -65, -64, 30, 48, 8, 63, 22,
    -46, 100, -92, -126, 89, 48, 105, 96, 89, -50,
    -81, -120, -15, 38, 85, 62, 120, -24, -128, -6,
    23, -65, 92, 106, 1, 10, 68, 60, 41, 36,
    -102, 49, -109, -84, -45, 49, -29, -64, -90, -121,
    73, -76, 10, -55, -121, 48, 82, 104, 24, 121,
    -112, 3, -2, -85, -22, 44, -39, -3, -112, 16,
    6, 82, 0, -52, 4, 16, 69, -48, 40, -24,
    -50, -46, -116, 84, 79, -11, 30, -40, 104, 61,
    -105, 36, 96, -84, -119, -31, -91, -122, 37, 45,
    -124, -127, -93, 2, -24, 72, 123, 105, 67, -55,
    -93, 119, 33, -84, 43, -81, -93, 77, 44, 73,
    38, 104, -56, -95, -104, -125, -124, 12, -87, -41,
    68, 3, 75, 72, -120, -69, 56, 67, -114, 105,
    62, -2, -44, 71, 104, 77, 126, -78, -12, 61,
    -29, 77, -64, 59, -80, 109, 27, -59, -122, -46,
    61, -88, -18, -19, 4, 77, 31, 115, -124, -58,
    2, 100, -114, -13, 32, 88, -125, -28, -57, 118,
    45, 116, 116, 42, -46, -48, -16, 21, -106, 6,
    -117, -121, 53, 52, -45, 6, -104, 2, 121, -91,
    126, -77, 108, -77, 28, -7, 93, 60, -114, -101,
    -86, 5, 41, 77, -26, -24, 61, -40, -18, -105,
    -23, 122, 45, 81, -32, 70, -36, -109, -65, -16,
    -125, 98, 66, -14, -56, 119, 40, 7, -91, 14,
    35, 44, 33, -33, -4, -62, 28, 13, -112, 127,
    119, 60, -125, 88, 116, -88, -125, -103, -92, 94,
    -56, -126, -42, 97, 109, -120, 110, 91, -20, -85,
    -102, 97, -111, -65, -99, -66, -93, 62, -110, 23,
    -101, -65, -95, -51, -24, 79, -44, 29, -38, -43,
    18, -63, -106, -88, 125, -16, 34, -108, 124, 120,
    -34, 63, -21, -85, 127, -32, 121, 22, 71, -37,
    23, -63, 4, -66, -13, 86, 92, 26, -33, -63,
    58, -40, 3, 76, 106, 24, 123, 19, 95, -8,
    4, -1, -107, -27, 70, 94, 90, 54, 27, -31,
    -27, -99, -110, 127, 98, 114, -102, -20, -22, -61,
    115, -128, -109, 15, 12, -64, -69, 102, 7, -101,
    14, 28, -40, -81, 78, -98, 60, 69, 103, 43,
    -112, 107, -21, 44, -39, -96, 11, 76, -88, -83,
    57, 34, -78, 78, 14, 42, 33, -74, 116, 20,
    -86, -80, 57, 81, -32, 57, 36, -13, 25, 105,
    -104, 6, 48, 9, -76, 80, -61, -32, 66, 43,
    100, 11, 124, 113, 11, -28, -38, 71, 19, 12,
    83, 74, -54, -47, 73, 22, 121, -118, -38, -127,
    1, 22, 16, -66, -128, 2, 121, -52, -68, 75,
    53, 111, 50, 28, 104, 80, -26, 66, 126, -106,
    67, -82, -73, -16, -125, -89, 14, 110, 107, 111,
    73, 40, 44, 73, -39, 24, -60, 66, -47, 87,
    -17, 64, -65, -104, -49, 53, 17, -16, -15, -82,
    -74, 14, 82, -62, 104, -79, -53, 30, 116, -44,
    -110, -20, -27, -58, 100, 33, -31, 123, -92, -2,
    -101, -103, -48, -57, 82, 103, 105, 95, -67, 127,
    61, -76, 30, -6, 7, 116, 14, -81, -72, 69,
    -48, -59, 115, -44, -96, 73, 55, 25, 30, 106,
    -62, 3, 0, -22, 98, -116, -46, 66, 27, 123,
    15, 122, 16, -116, 10, 102, 65, 71, -105, -56,
    5, 51, 91, 5, -56, 69, -77, 14, 113, 1,
    30, 15, 31, 62, 66, 10, -72, -67, 116, -56,
    -41, -40, -74, 7, 77, 58, -2, 66, 20, 65,
    100, 87, -120, -26, 80, -98, 53, 21, -29, 83,
    -83, 14, 74, 6, -92, 2, -127, 19, 14, -13,
    36, 101, -56, 61, -120, -23, -26, 111, 92, 118,
    12, -83, 57, -3, -66, -123, -68, -10, -59, 109,
    19, 71, -95, 104, 42, 67, 67, -95, -75, -28,
    -16, 6, 120, 26, 27, -24, 60, 40, 112, 65,
    -20, -27, 21, -51, 105, 101, -51, 34, 26, 74,
    6, -111, 10, 51, 40, -18, -31, -81, -103, -28,
    -14, 110, -13, 59, 126, 3, -96, -63, 93, -91,
    -72, -49, 110, -69, -44, 124, -4, -34, 77, 126,
    -41, -55, 119, 31, 30, 74, 56, -97, 74, -22,
    103, 57, 0, 62, -75, -89, 64, -15, -69, -47,
    -45, 116, -120, 5, 79, -94, 84, -122, -108, -121,
    -103, -65, 73, -69, -111, -89, -34, 63, 8, 99,
    -2, -6, 39, 64, 51, 22, 77, 51, 29, 21,
    -55, 109, 76, -104, 88, 113, -31, 73, 48, 41,
    8, 22, -15, -9, -15, 39, -34, 79, 10, -73,
    -92, 20, 127, -93, -124, 73, -28, 115, -122, 100,
    65, -114, 23, 9, 88, -51, 10, -31, 119, 1,
    120, -80, -31, 62, 8, 85, 64, -118, 56, 83,
    -76, -35, 13, -98, 59, -115, 20, 73, 21, 71,
    -128, -88, 101, 82, -112, -25, 96, -72, 71, 30,
    -85, 65, 88, 126, -89, -84, 105, -14, -92, -87,
    -104, -46, 10, 11, 0, 25, -121, 49, 2, 48,
    79, -64, 55, 69, 19, 79, 59, -51, -96, -51,
    -8, -126, 36, -128, 22, -49, 54, -74, 104, 5,
    -66, -13, -102, 95, 38, -118, -30, 119, 0, 85,
    -98, -18, 33, -113, 76, 12, 93, -99, 93, 37,
    40, -98, -109, 1, -108, -8, -18, -16, -82, 106,
    -75, 85, -82, 56, 9, -63, -68, 10, -128, -125,
    48, -79, 116, -76, -109, 104, 78, 96, -121, 23,
    -22, -114, -10, -103, -128, -58, 123, -123, 80, -15,
    -109, 18, -98, -111, 58, -30, 62, -98, -57, -7,
    104, 27, 62, 35, -95, -50, -88, 11, -22, 81,
    -97, -11, -43, 63, 5, 98, 114, 54, 45, -125,
    -123, -42, 5, 43, 2, 90, 96, 66, 48, -126,
    29, -76, 65, 5, 33, -106, 5, -52, 54, -58,
    59, 38, -124, 48, 108, -30, 119, -63, -109, -128,
    119, -95, -63, -104, -114, -124, 3, -37, 37, -96,
    -42, 122, -40, 29, -90, 31, 88, 38, -30, 107,
    -43, 61, -60, -43, 89, 114, 126, 97, -51, 30,
    -73, 56, -70, -75, -123, 118, -58, -32, 55, -20,
    -31, -99, 50, 44, 110, 107, 110, -34, 74, 34,
    -14, -126, -56, 71, -57, -58, -7, 119, 16, 51,
    -126, -93, 93, -16, 62, 89, -57, -96, 12, 113,
    59, 120, -16, 32, -97, 81, -2, -10, -107, -73,
    -52, -20, 40, -27, -95, 50, 17, 33, -122, -64,
    -65, -40, 105, -125, 89, -111, -35, 101, -55, -61,
    30, -63, -11, -24, 123, 3, -83, -51, -37, 90,
    50, 32, 64, 64, -70, 32, 71, 79, -82, 71,
    -106, 32, -76, 111, -120, 63, -77, -104, 67, -128,
    93, -11, 46, 35, 68, -101, -47, -82, -79, 0,
    -98, -59, -38, 23, 38, 54, 0, 9, 121, -80,
    -67, -91, -123, 54, 5, 80, -3, 1, 108, 92,
    -121, 62, -62, 114, -126, -105, 20, -60, -107, -111,
    107, -18, -27, 69, -110, 16, 104, 114, -61, 59,
    72, -102, -127, -88, -123, 29, 68, 16, -53, -51,
    91, 103, 71, 103, 73, 121, -126, -9, -93, -2,
    -10, 70, 16, 12, -42, 65, -76, 114, -65, -29,
    14, 100, 0, -64, -45, 24, -36, 126, -5, -19,
    -2, 89, -121, -3, 51, 55, 79, 103, -11, 17,
    -77, -103, -71, -72, 62, -57, 119, -95, 117, -48,
    -58, 17, -128, 6, -18, 56, -4, 19, -47, 16,
    -128, -10, 48, -34, -30, 75, 15, -128, 119, 17,
    -38, -37, 59, 24, 107, 56, 88, 100, 102, 118,
    -124, 69, -90, 11, -104, -44, -24, 25, 126, -66,
    89, -63, 16, 24, 34, -29, -38, 60, 65, 69,
    -102, -7, 23, 6, 117, -88, -30, -59, -80, 94,
    -122, -95, 25, 11, 32, -110, -5, 16, 83, -59,
    79, -14, 55, 82, 35, 101, -81, 43, 52, -24,
    50, -54, -13, -14, -100, -68, 11, 96, 18, 49,
    -38, 68, -8, 0, 3, -60, 56, -71, 20, -115,
    -126, -8, 43, 54, 72, -4, -114, -109, 38, -15,
    -124, -33, 70, 39, -87, -120, 36, 33, -21, 85,
    29, 31, -10, 89, -34, 111, -81, -7, -19, -66,
    -5, -112, 76, -37, -90, -76, -39, -52, 11, -113,
    120, 76, -59, -67, -49, 124, 70, -42, -34, -94,
    -128, 14, -63, 76, 104, 15, -6, -60, 60, -107,
    -49, -44, -5, 71, -9, -46, 122, -22, 31, 115,
    76, -102, 70, 61, 77, -102, -57, 103, -63, 4,
    9, 86, 4, 59, -8, 107, 99, 10, 75, 97,
    22, -101, 120, -76, -79, 100, -45, -18, -1, 4,
	};

}
