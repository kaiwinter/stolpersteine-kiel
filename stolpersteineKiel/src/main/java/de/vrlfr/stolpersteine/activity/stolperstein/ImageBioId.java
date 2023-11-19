package de.vrlfr.stolpersteine.activity.stolperstein;

/**
 * Value Objekt, das für die Gruppierung der Stolpersteine nach Bild ID und Biografie ID dient.
 */
public final class ImageBioId {
	private final int imageId;
	private final int bioId;

	public ImageBioId(int imageId, int bioId) {
		this.imageId = imageId;
		this.bioId = bioId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bioId;
		result = prime * result + imageId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageBioId other = (ImageBioId) obj;
		if (bioId != other.bioId)
			return false;
		return imageId == other.imageId;
	}
}
